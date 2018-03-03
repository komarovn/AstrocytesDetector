/*
 * Copyright (c) Lobachevsky University, 2017. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal with the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * Developed by: Komarov Nikolay.
 */
package com.astrocytes.application.widgets;

import com.astrocytes.application.resources.ApplicationConstants;
import com.astrocytes.core.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;

public class GraphicalWidget extends JPanel {
    private final int DEFAULT_ZOOM_LEVEL = 3;

    private BufferedImage image;
    protected BufferedImage currentView;

    protected Integer currentX, currentY;
    private Integer widthWidget;
    private Integer heightWidget;
    protected Integer widthImage;
    protected Integer heightImage;

    private Boolean panEnabled = true;
    private Boolean zoomEnabled = true;
    protected int zoomLevel;
    private java.util.List<Double> zoomLevels =
            new ArrayList<Double>(Arrays.asList(0.125, 0.25, 0.5, 1.0, 2.0, 4.0, 8.0));
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
        widthWidget = width == null ? ApplicationConstants.DEFAULT_GRAPHICAL_WIDGET_WIDTH : width;
        heightWidget = height == null ? ApplicationConstants.DEFAULT_GRAPHICAL_WIDGET_HEIGHT : height;
        resetZoom();
        setSize(new Dimension(widthWidget, heightWidget));
        setPreferredSize(new Dimension(widthWidget, heightWidget));
        addListeners();
    }

    private void addListeners() {
        GraphicalWidgetListener listener = new GraphicalWidgetListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentView != null) {
            g.drawImage(currentView, 0, 0, this);
        }
    }

    private void resetZoom() {
        zoomLevel = DEFAULT_ZOOM_LEVEL;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            if (widthWidget >= image.getWidth()) {
                widthImage = image.getWidth();
            }

            if (heightWidget >= image.getHeight()) {
                heightImage = image.getHeight();
            }

            resetZoom();
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
        updateCurrentViewWithoutRepaint(0, 0);
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
        } else {
            widthImage = width;
            heightImage = height;
        }

        updateWidget();
        setPreferredSize(new Dimension(widthWidget, heightWidget));
        repaint();
    }

    private void updateCurrentView(int deltaX, int deltaY) {
        updateCurrentViewWithoutRepaint(deltaX, deltaY);
        repaint();
    }

    private void updateCurrentViewWithoutRepaint(int deltaX, int deltaY) {
        currentX += deltaX;
        currentY += deltaY;
        currentX = currentX >= 0 ? currentX : 0;
        currentY = currentY >= 0 ? currentY : 0;
        currentX = zoomedImage.getWidth() - currentX > widthImage ? currentX : zoomedImage.getWidth() - widthImage;
        currentY = zoomedImage.getHeight() - currentY > heightImage ? currentY : zoomedImage.getHeight() - heightImage;
        System.out.println("currentX = " + currentX + "; currentY = " + currentY);
        currentView = zoomedImage.getSubimage(currentX, currentY, widthImage, heightImage);
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
        return zoomLevels.get(zoomLevel);
    }

    public void lockZoomAndPan() {
        zoomEnabled = false;
        panEnabled = false;
    }

    public void unlockZoomAndPan() {
        zoomEnabled = true;
        panEnabled = true;
    }

    protected BufferedImage getZoomedImage() {
        return zoomedImage;
    }

    protected void destroy() {
        image = null;
        zoomedImage = null;
        currentView = null;
        currentX = currentY = 0;
        resetZoom();
    }

    protected class GraphicalWidgetListener extends MouseAdapter {
        private Integer startPointX, startPointY;
        private Integer endPointX, endPointY;
        protected int deltaX, deltaY;
        protected Boolean isZoomIn;
        protected Integer currentXOld, currentYOld;
        protected int previousZoomLevel;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            if (e.getButton() == MouseEvent.BUTTON1 && image != null && panEnabled) {
                startPointX = e.getX();
                startPointY = e.getY();
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            int buttonKey = MouseEvent.BUTTON1_DOWN_MASK;

            if ((e.getModifiersEx() & buttonKey) == buttonKey && image != null && panEnabled) {
                deltaX = startPointX - e.getX();
                deltaY = startPointY - e.getY();
                updateCurrentView(deltaX, deltaY);
                startPointX = e.getX();
                startPointY = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            if (e.getButton() == MouseEvent.BUTTON1 && image != null && panEnabled) {
                endPointX = e.getX();
                endPointY = e.getY();
                deltaX = startPointX - endPointX;
                deltaY = startPointY - endPointY;
                updateCurrentView(deltaX, deltaY);
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            if (zoomEnabled && image != null) {
                int notches = e.getWheelRotation();
                previousZoomLevel = zoomLevel;

                if (notches > 0) {
                    if (previousZoomLevel != 0) {
                        zoomLevel--;
                        isZoomIn = false;
                        changeZoomedImage();
                    }
                }

                if (notches < 0) {
                    if (previousZoomLevel != zoomLevels.size() - 1) {
                        zoomLevel++;
                        isZoomIn = true;
                        changeZoomedImage();
                    }
                }
            }
        }

        private void changeZoomedImage() {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(getZoomScale(), getZoomScale());
            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR/*AffineTransformOp.TYPE_BICUBIC*/);
            int w = (int) (image.getWidth() * getZoomScale());
            int h = (int) (image.getHeight() * getZoomScale());
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

            int deltaX1 = (centerXAfter - widthImage / 2) - currentX;
            int deltaY1 = (centerYAfter - heightImage / 2) - currentY;

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
            currentXOld = currentX.intValue();
            currentYOld = currentY.intValue();
            updateCurrentView(deltaX1, deltaY1);
        }
    }

}
