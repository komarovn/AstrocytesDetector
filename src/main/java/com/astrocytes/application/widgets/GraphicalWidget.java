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
    private final int DEFAULT_ZOOM_LEVEL = 9;

    private BufferedImage image;
    protected BufferedImage currentView;

    private Integer currentX, currentY;
    private Integer widgetWidth;
    private Integer widgetHeight;
    protected Integer imageWidth;
    protected Integer imageHeight;

    private Boolean panEnabled = true;
    private Boolean zoomEnabled = true;
    protected int zoomLevel;
    private java.util.List<Double> zoomLevels =
            new ArrayList<Double>(Arrays.asList(0.125, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.2,
                    1.4, 1.6, 1.8, 2.0, 2.5, 4.0, 5.0, 6.0, 7.0, 8.0));
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
        this.currentX = 0;
        this.currentY = 0;
        setWidgetSize(width, height);
        resetZoom();
        initListeners();
    }

    private void initListeners() {
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
            calculateZoomedImage();
            invalidateImageSize();
            updateCurrentView(0, 0);
            repaint();
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

    protected void setCurrentView(BufferedImage newCurrentView) {
        currentView = newCurrentView;
        updateWidget();
    }

    public void updateWidget() {
        updateUI();
    }

    /**
     * Set size of the graphical widget
     *
     * @param width - width of widget;
     * @param height - height of widget.
     */
    public void setWidgetSize(Integer width, Integer height) {
        this.widgetWidth = width == null ? ApplicationConstants.DEFAULT_GRAPHICAL_WIDGET_WIDTH : width;
        this.widgetHeight = height == null ? ApplicationConstants.DEFAULT_GRAPHICAL_WIDGET_HEIGHT : height;
        setPreferredSize(new Dimension(this.widgetWidth, this.widgetHeight));
        setSize(new Dimension(this.widgetWidth, this.widgetHeight));
        invalidateImageSize();
        updateWidget();
        updateCurrentView(0, 0);
        repaint();
    }

    private void invalidateImageSize() {
        this.imageWidth = this.image == null ? 0 : Math.min(this.zoomedImage == null ?
                this.image.getWidth() : this.zoomedImage.getWidth(), this.widgetWidth);
        this.imageHeight = this.image == null ? 0 : Math.min(this.zoomedImage == null ?
                this.image.getHeight() : this.zoomedImage.getHeight(), this.widgetHeight);
        System.out.println("imageWidth = " + imageWidth + ", " + imageHeight);
    }

    private void updateCurrentView(int deltaX, int deltaY) {
        if (image != null) {
            currentX = Math.max(currentX + deltaX, 0);
            currentY = Math.max(currentY + deltaY, 0);
            currentX = zoomedImage.getWidth() - currentX > imageWidth ? currentX : zoomedImage.getWidth() - imageWidth;
            currentY = zoomedImage.getHeight() - currentY > imageHeight ? currentY : zoomedImage.getHeight() - imageHeight;
            currentView = zoomedImage.getSubimage(currentX, currentY, imageWidth, imageHeight);
            System.out.println("currentX = " + currentX + "; currentY = " + currentY);
        }
    }

    /**
     * Enable or disable zoom option for graphical widget.
     *
     * @param isEnabled - if true, enable zoom, false otherwise.
     */
    public void setZoomEnabled(Boolean isEnabled) {
        zoomEnabled = isEnabled;
    }

    public Double getZoomValue() {
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

    public Integer getOffsetX() {
        return currentX;
    }

    public Integer getOffsetY() {
        return currentY;
    }

    protected BufferedImage getZoomedImage() {
        return zoomedImage;
    }

    private void calculateZoomedImage() {
        int width = (int) (this.image.getWidth() * getZoomValue());
        int height = (int) (this.image.getHeight() * getZoomValue());
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(getZoomValue(), getZoomValue());
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR/*AffineTransformOp.TYPE_BICUBIC*/);
        this.zoomedImage = affineTransformOp.filter(ImageHelper.cloneBufferedImage(this.image),
                new BufferedImage(width, height, this.image.getType()));
    }

    protected void reset() {
        this.image = null;
        this.zoomedImage = null;
        this.currentView = null;
        this.currentX = this.currentY = 0;
        resetZoom();
    }

    protected class GraphicalWidgetListener extends MouseAdapter {
        private Integer startPointX, startPointY;
        private Integer endPointX, endPointY;
        protected int deltaX, deltaY;
        protected int previousZoomLevel;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            if (panEnabled && image != null && e.getButton() == MouseEvent.BUTTON1) {
                startPointX = e.getX();
                startPointY = e.getY();
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            int buttonKey = MouseEvent.BUTTON1_DOWN_MASK;

            if (panEnabled && image != null && (e.getModifiersEx() & buttonKey) == buttonKey) {
                deltaX = startPointX - e.getX();
                deltaY = startPointY - e.getY();
                updateCurrentView(deltaX, deltaY);
                repaint();
                startPointX = e.getX();
                startPointY = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            if (panEnabled && image != null && e.getButton() == MouseEvent.BUTTON1) {
                endPointX = e.getX();
                endPointY = e.getY();
                deltaX = startPointX - endPointX;
                deltaY = startPointY - endPointY;
                updateCurrentView(deltaX, deltaY);
                repaint();
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            if (zoomEnabled && image != null) {
                int notches = e.getWheelRotation();
                previousZoomLevel = zoomLevel;

                if (notches > 0) {
                    if (zoomLevel != 0) {
                        zoomLevel--;
                        changeZoomedImage(getZoomValue() / zoomLevels.get(previousZoomLevel));
                    }
                }

                if (notches < 0) {
                    if (zoomLevel != zoomLevels.size() - 1) {
                        zoomLevel++;
                        changeZoomedImage(getZoomValue() / zoomLevels.get(previousZoomLevel));
                    }
                }
            }
        }

        private void changeZoomedImage(double zoomCoeff) {
            int centerXBefore = currentX + imageWidth / 2;
            int centerYBefore = currentY + imageHeight / 2;

            calculateZoomedImage();
            invalidateImageSize();

            int deltaX = (int) (centerXBefore * zoomCoeff - imageWidth / 2) - currentX;
            int deltaY = (int) (centerYBefore * zoomCoeff - imageHeight / 2) - currentY;
            currentView = ImageHelper.cloneBufferedImage(zoomedImage);
            updateCurrentView(deltaX, deltaY);
            repaint();
        }
    }

}
