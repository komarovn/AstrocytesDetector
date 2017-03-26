package com.astrocytes.client.widgets;

import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.resources.ClientConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Nikolay Komarov on 24.02.2017.
 */
public class GraphicalWidget extends JPanel {

    private BufferedImage image;
    protected BufferedImage currentView;

    private Integer currentX, currentY;
    private Integer widthWidget;
    private Integer heightWidget;
    private Integer widthImage;
    private Integer heightImage;

    private Boolean zoomEnabled = true;
    private Double zoomScale;
    private java.util.List<Double> zoomLevels = new ArrayList<>(Arrays.asList(new Double[]{0.125, 0.25, 0.5, 1.0, 2.0, 4.0, 8.0}));
    private BufferedImage zoomedImage;

    /**
     * Default constructor. Creates a new graphical widget with default size 500 px for width and 500 px for height.
     */
    public GraphicalWidget() {
        this(null, null);
    }

    /**
     * Create a new graphical widget with specific size.
     *
     * @param width - a width of creating graphical widget;
     * @param height - a height of creating graphical widget;
     */
    public GraphicalWidget(Integer width, Integer height) {
        currentX = 0;
        currentY = 0;
        widthWidget = width == null ? ClientConstants.DEFAULT_GRAPHICAL_WIDGET_WIDTH : width;
        heightWidget = height == null ? ClientConstants.DEFAULT_GRAPHICAL_WIDGET_HEIGHT : height;
        resetZoomScale();
        setSize(new Dimension(widthWidget, heightWidget));
        setPreferredSize(new Dimension(widthWidget, heightWidget));
        addListeners();
    }

    private void addListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            Integer startPointX, startPointY;
            Integer endPointX, endPointY;
            Boolean isZoomIn;

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
                    Integer deltaX = startPointX - endPointX;
                    Integer deltaY = startPointY - endPointY;
                    updateCurrentView(deltaX, deltaY);
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                if (zoomEnabled && image != null) {
                    int notches = e.getWheelRotation();
                    if (notches > 0) {
                        int currentIndex = zoomLevels.indexOf(zoomScale);
                        if (currentIndex != 0) {
                            zoomScale = zoomLevels.get(currentIndex - 1);
                            isZoomIn = false;
                            changeZoomedImage();
                        }
                    }
                    if (notches < 0) {
                        int currentIndex = zoomLevels.indexOf(zoomScale);
                        if (currentIndex != zoomLevels.size() - 1) {
                            zoomScale = zoomLevels.get(currentIndex + 1);
                            isZoomIn = true;
                            changeZoomedImage();
                        }
                    }
                }
            }

            private void changeZoomedImage() {
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.scale(zoomScale, zoomScale);
                AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
                int w = (int) (image.getWidth() * zoomScale);
                int h = (int) (image.getHeight() * zoomScale);
                int centerXBefore = currentX + widthImage / 2;
                int centerYBefore = currentY + heightImage / 2;
                zoomedImage = new BufferedImage(w, h, image.getType());
                zoomedImage = affineTransformOp.filter(ImageHelper.cloneBufferedImage(image), zoomedImage);
                int centerXAfter = centerXBefore * 2;
                int centerYAfter = centerYBefore * 2;
                if (!isZoomIn) {
                    centerXAfter = centerXBefore / 2;
                    centerYAfter = centerYBefore / 2;
                }
                int deltaX = (centerXAfter - widthImage / 2) - currentX;
                int deltaY = (centerYAfter - heightImage / 2) - currentY;

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
                updateCurrentView(deltaX, deltaY);
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

    private void resetZoomScale() {
        zoomScale = zoomLevels.get(3);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            if (widthWidget >= image.getWidth()) {
                widthImage = image.getWidth();
            } else {
                widthImage = widthWidget;
            }
            if (heightWidget >= image.getHeight()) {
                heightImage = image.getHeight();
            } else {
                heightImage = heightWidget;
            }
            resetZoomScale();
            zoomedImage = ImageHelper.cloneBufferedImage(this.image);
            updateCurrentView(0, 0);
        }
    }

    /**
     * Get the full image displayed on graphical widget.
     *
     * @return an image which is currently displayed on whidget or <code>null</code>, if there is no image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Get the current view displayed on graphical widget.
     *
     * @return an image of the current view which is displayed on widget or <code>null</code>, if there is
     *         no image to display.
     */
    public BufferedImage getCurrentView() {
        updateCurrentView(0, 0);
        BufferedImage currentViewCropped = new BufferedImage(currentView.getColorModel(),
                currentView.getRaster().createCompatibleWritableRaster(currentView.getWidth(), currentView.getHeight()),
                currentView.isAlphaPremultiplied(), null);
        currentView.copyData(currentViewCropped.getRaster());
        return currentViewCropped;
    }

    public void setCurrentView(BufferedImage newCurrentView) {
        currentView = newCurrentView;
        updateWidget();
    }

    public void updateWidget() {
        updateUI();
    }

    /**
     * Resizing of the graphical widget
     *
     * @param width - a new size parameter for width;
     * @param height - a new size parameter for height.
     */
    public void updateWidget(Integer width, Integer height) {
        setSize(new Dimension(width, height));
        widthWidget = width;
        heightWidget = height;
        if (image != null) {
            setImage(image);
        }
        else {
            widthImage = width;
            heightImage = height;
        }
        updateWidget();
        setPreferredSize(new Dimension(widthWidget, heightWidget));
        repaint();
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

    /**
     * Enable or disable zoom option for graphical widget.
     *
     * @param isEnabled - if true, enable zoom, false otherwise.
     */
    public void setZoomEnabled(Boolean isEnabled) {
        zoomEnabled = isEnabled;
    }

    public Double getZoomScale() {
        return zoomScale;
    }
}
