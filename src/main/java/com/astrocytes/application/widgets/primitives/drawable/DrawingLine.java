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

public class DrawingLine extends SimpleLine implements Paintable {
    private Color objectColor;

    public DrawingLine() {
        this(Color.MAGENTA);
    }

    public DrawingLine(Color color) {
        super();
        this.objectColor = color;
    }

    public DrawingLine(Double xStart, Double yStart, Double xEnd, Double yEnd) {
        this(xStart, yStart, xEnd, yEnd, Color.MAGENTA);
    }

    public DrawingLine(Double xStart, Double yStart, Double xEnd, Double yEnd, Color color) {
        super(xStart, yStart, xEnd, yEnd);
        this.objectColor = color;
    }

    @Override
    public void setColor(Color color) {
        this.objectColor = color;
    }

    @Override
    public void paint(Graphics2D graphics, int shiftX, int shiftY, double zoomScale) {
        if (isFull()) {
            graphics.setPaint(objectColor);
            graphics.setStroke(new BasicStroke(zoomScale < 1.0 ? 1 : (int) (2 * zoomScale)));

            float xStart = (float) (zoomScale * getxStart() - shiftX);
            float yStart = (float) (zoomScale * getyStart() - shiftY);
            float xEnd = (float) (zoomScale * getxEnd() - shiftX);
            float yEnd = (float) (zoomScale * getyEnd() - shiftY);

            graphics.draw(new Line2D.Float(xStart, yStart, xEnd, yEnd));
        }
    }
}
