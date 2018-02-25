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
package com.astrocytes.application.widgets.primitives.drawable;

import com.astrocytes.application.widgets.primitives.SimpleLine;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class DrawingLine extends SimpleLine implements Paintable {

    private boolean drawing = true;
    private double zoomLevel = 1.0;
    private Color objectColor = Color.MAGENTA;
    private Color drawingColor = Color.ORANGE;

    public DrawingLine() {
        super();
    }

    public DrawingLine(Double xStart, Double yStart, Double xEnd, Double yEnd) {
        super(xStart, yStart, xEnd, yEnd);
    }

    public void setDrawing(boolean isDrawng) {
        drawing = isDrawng;
    }

    public boolean isDrawing() {
        return drawing;
    }

    @Override
    public void paint(BufferedImage target, Graphics2D graphics) {
        if (isFull()) {
            graphics.setPaint(isDrawing() ? drawingColor : objectColor);
            graphics.setStroke(new BasicStroke(2));

            //setxEnd((double) (getImage().getWidth() < currentView.getWidth() ?
            //       Math.max(getImage().getWidth(), getWidth()) : currentView.getWidth() - 1));

            graphics.draw(new Line2D.Float(getxStart().floatValue(), getyStart().floatValue(),
                    getxEnd().floatValue(), getyEnd().floatValue()));
        }
    }

    @Override
    public void move(int deltaX, int deltaY) {
        if (isFull()) {
            xStart += deltaX;
            xEnd += deltaX;
            yStart += deltaY;
            yEnd += deltaY;
        }
    }

    @Override
    public void updateZoom(double zoomLevel) {
        double zoomDelta = zoomLevel / this.zoomLevel;
        this.zoomLevel = zoomLevel;

        if (isFull()) {
            //double yNew = zoomDelta * (getyEnd() + currentYOld) - currentY;
            //setyEnd(yNew);
            //setyStart(yNew);
        }
    }
}
