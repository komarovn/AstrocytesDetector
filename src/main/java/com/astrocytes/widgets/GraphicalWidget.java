package com.astrocytes.widgets;

import com.astrocytes.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Nikolay Komarov on 24.02.2017.
 */
public class GraphicalWidget extends JPanel {

    private BufferedImage image;
    private BufferedImage currentView;

    private Integer currentX, currentY;
    private Integer widthWidget = 1920;
    private Integer heightWidget = 1080;
    private Integer widthImage = 1920;
    private Integer heightImage = 1080;

    private Double zoomScale;
    private java.util.List<Double> zoomLevels = new ArrayList<>(Arrays.asList(new Double[]{0.125, 0.25, 0.5, 1.0, 2.0, 4.0, 8.0}));
    private BufferedImage zoomedImage;

    public GraphicalWidget() {
        currentX = 0;
        currentY = 0;
        zoomScale = zoomLevels.get(3);
        setSize(new Dimension(widthWidget, heightWidget));
        setMaximumSize(new Dimension(widthWidget, heightWidget));
        MouseAdapter mouseAdapter = new MouseAdapter() {
            Integer startPointX, startPointY;
            Integer endPointX, endPointY;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1 && image != null) {
                    mouseClicked(e);
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (image != null) {
                    int deltaX = startPointX - e.getX();
                    int deltaY = startPointY - e.getY();
                    updateCurrentView(deltaX, deltaY);
                    startPointX = e.getX();
                    startPointY = e.getY();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (image != null) {
                    startPointX = e.getX();
                    startPointY = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (image != null) {
                    endPointX = e.getX();
                    endPointY = e.getY();
                    int deltaX = startPointX - endPointX;
                    int deltaY = startPointY - endPointY;
                    updateCurrentView(deltaX, deltaY);
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                if (image != null) {
                    int notches = e.getWheelRotation();
                    if (notches > 0) {
                        int currentIndex = zoomLevels.indexOf(zoomScale);
                        if (currentIndex != 0) {
                            zoomScale = zoomLevels.get(currentIndex - 1);
                            changeZoomedImage();
                        }
                    }
                    if (notches < 0) {
                        int currentIndex = zoomLevels.indexOf(zoomScale);
                        if (currentIndex != zoomLevels.size() - 1) {
                            zoomScale = zoomLevels.get(currentIndex + 1);
                            changeZoomedImage();
                        }
                    }
                }
            }

            private void changeZoomedImage() {
                if (zoomScale.equals(zoomLevels.get(3))) {
                    zoomedImage = ImageHelper.cloneBufferedImage(image);
                }
                else {
                    AffineTransform affineTransform = new AffineTransform();
                    affineTransform.scale(zoomScale, zoomScale);
                    AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
                    int w = (int) (image.getWidth() * zoomScale);
                    int h = (int) (image.getHeight() * zoomScale);
                    zoomedImage = new BufferedImage(w, h, image.getType());
                    zoomedImage = affineTransformOp.filter(ImageHelper.cloneBufferedImage(image), zoomedImage);
                }
                if (widthWidget >= zoomedImage.getWidth()) {
                    widthImage = zoomedImage.getWidth();
                }
                else {
                    widthImage = widthWidget;
                }
                if (heightWidget >= zoomedImage.getHeight()) {
                    heightImage = zoomedImage.getHeight();
                }
                else {
                    heightImage = heightWidget;
                }
                currentView = ImageHelper.cloneBufferedImage(zoomedImage);
                updateCurrentView((int) (widthImage / zoomScale), (int) (heightImage / zoomScale));
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentView != null) {
            g.drawImage(currentView, 0, 0, this);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (widthWidget >= image.getWidth()) {
            widthImage = image.getWidth();
        }
        else {
            widthImage = widthWidget;
        }
        if (heightWidget >= image.getHeight()) {
            heightImage = image.getHeight();
        }
        else {
            heightImage = heightWidget;
        }
        zoomedImage = ImageHelper.cloneBufferedImage(this.image);
        updateCurrentView(0, 0);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void updateWidget() {
        updateUI();
    }

    private void updateCurrentView(int deltaX, int deltaY) {
        currentX += deltaX;
        currentY += deltaY;
        currentX = currentX >= 0 ? currentX : 0;
        currentY = currentY >= 0 ? currentY : 0;
        currentX = zoomedImage.getWidth() - currentX > widthImage ? currentX : zoomedImage.getWidth() - widthImage;
        currentY = zoomedImage.getHeight() - currentY > heightImage ? currentY : zoomedImage.getHeight() - heightImage;
        currentView = zoomedImage.getSubimage(currentX, currentY, widthImage, heightImage);
        updateWidget();
    }
}
