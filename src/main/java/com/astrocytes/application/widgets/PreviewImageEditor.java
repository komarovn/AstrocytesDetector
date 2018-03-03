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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public abstract class PreviewImageEditor extends ImageEditor {

    private BufferedImage originalImage;
    private Integer widthWidget;
    private Integer heightWidget;

    private MouseAdapter mouseAdapter;

    public PreviewImageEditor(Integer width, Integer height) {
        super(width, height);
        widthWidget = width == null ? ApplicationConstants.PREVIEW_WINDOW_WIDTH : width;
        heightWidget = height == null ? ApplicationConstants.PREVIEW_WINDOW_HEIGHT : height;
        //updateWidget(width, height);
        setZoomEnabled(false);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        addListenerForPreview();
    }

    public PreviewImageEditor(Integer width, Integer height, BufferedImage originalImage) {
        this(width, height);
        this.originalImage = originalImage;
    }

    private void addListenerForPreview() {
        removeMouseListener(getMouseListeners()[0]);
        removeMouseMotionListener(getMouseMotionListeners()[0]);
        mouseAdapter = new PreviewImageEditorListener();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public abstract void processPreviewImage();

    public void updatePreview(BufferedImage newCurrentView) {
        setCurrentView(newCurrentView);
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    public BufferedImage getOriginalImageView() {
        BufferedImage originalImageView = originalImage.getSubimage(currentX, currentY, imageWidth, imageHeight);
        BufferedImage originalImageViewCropped = new BufferedImage(originalImageView.getColorModel(),
                originalImageView.getRaster().createCompatibleWritableRaster(originalImageView.getWidth(),
                        originalImageView.getHeight()),
                originalImageView.isAlphaPremultiplied(), null);
        originalImageView.copyData(originalImageViewCropped.getRaster());
        return originalImageViewCropped;
    }

    private class PreviewImageEditorListener extends ImageEditorListener {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (currentView != null) {
                processPreviewImage();
            }
        }
    }

    public Integer getOffsetX() {
        return currentX;
    }

    public Integer getOffsetY() {
        return currentY;
    }

}
