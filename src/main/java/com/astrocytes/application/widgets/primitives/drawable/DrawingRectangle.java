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
package com.astrocytes.application.widgets.primitives.drawable;

import com.astrocytes.application.widgets.primitives.SimpleRectangle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class DrawingRectangle extends SimpleRectangle implements Paintable {

    private double zoomLevel = 1.0;
    private Color drawingColor = Color.BLUE;

    @Override
    public void paint(BufferedImage target, Graphics2D graphics) {
        if (isFull()) {
            graphics.setPaint(drawingColor);
            graphics.setStroke(new BasicStroke(1));
            graphics.draw(new Rectangle2D.Float(getLeftX(), getTopY(), getWidth(), getHeight()));
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
        this.zoomLevel = zoomLevel;

        if (isFull()) {

        }
    }
}
