/*
 * Copyright (c) Lobachevsky University, 2018. All rights reserved.
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
package com.astrocytes.application.widgets.instrument;

import com.astrocytes.application.widgets.primitives.drawable.DrawingRectangle;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawRectangleInstrument extends Instrument {
    private boolean isDrawingState;
    private DrawingRectangle rectangle;
    private String rectangleKey;

    @Override
    public InstrumentType getType() {
        return InstrumentType.RECTANGLE;
    }

    @Override
    public void activate() {
        getEditor().lockZoomAndPan();
    }

    @Override
    public void onMouseDown(MouseEvent e) {
        this.rectangle = new DrawingRectangle(Color.ORANGE);
        int startX = (int) ((e.getX() + getEditor().getOffsetX()) / getEditor().getZoomValue());
        int startY = (int) ((e.getY() + getEditor().getOffsetY()) / getEditor().getZoomValue());
        this.rectangle.setStartPoint(startX, startY);
        this.rectangle.setEndPoint(startX, startY);
        getEditor().getLayerManager().getLayer(getRectanglesKey()).clear();
        getEditor().getLayerManager().getLayer(getRectanglesKey()).add(this.rectangle);
        this.isDrawingState = true;
    }

    @Override
    public void onMouseDrag(MouseEvent e) {
        if (isDrawingState) {
            updateRectangle(e.getX(), e.getY());
            getEditor().repaint();
        }
    }

    @Override
    public void onMouseUp(MouseEvent e) {
        updateRectangle(e.getX(), e.getY());
        this.isDrawingState = false;
        this.rectangle.setColor(Color.BLUE);
        getEditor().repaint();
    }

    private void updateRectangle(int endX, int endY) {
        if (getEditor().testPoint(endX, endY)) {
            endX = (int) ((endX + getEditor().getOffsetX()) / getEditor().getZoomValue());
            endY = (int) ((endY + getEditor().getOffsetY()) / getEditor().getZoomValue());
            this.rectangle.setEndPoint(endX, endY);
        }
    }

    private String getRectanglesKey() {
        if (this.rectangleKey == null) {
            this.rectangleKey = getEditor().getLayerManager().createLayer();
        }
        return this.rectangleKey;
    }

    public void setDrawingKey(String key) {
        this.rectangleKey = key;
    }
}
