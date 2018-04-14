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

import com.astrocytes.application.widgets.primitives.SimpleCircle;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class DrawingCircle extends SimpleCircle implements Paintable {
    private Color objectColor;
    private int strokeWidth = 2;

    public DrawingCircle(Double xCenter, Double yCenter, Double radius) {
        this(xCenter, yCenter, radius, Color.YELLOW);
    }

    public DrawingCircle(Double xCenter, Double yCenter, Double radius, Color color) {
        super(xCenter, yCenter, radius);
        this.objectColor = color;
    }

    @Override
    public void setColor(Color color) {
        this.objectColor = color;
    }

    @Override
    public Color getColor() {
        return this.objectColor;
    }

    @Override
    public void paint(Graphics2D graphics, int shiftX, int shiftY, double zoom) {
        if (isFull()) {
            graphics.setPaint(objectColor);
            graphics.setStroke(new BasicStroke(zoom < 1.0 ? 1 : (int) (strokeWidth * zoom)));

            float radius = (float) (zoom * getRadius());
            float xStart = (float) (zoom * getxCenter() - shiftX - radius / 2);
            float yStart = (float) (zoom * getyCenter() - shiftY - radius / 2);

            graphics.draw(new Ellipse2D.Float(xStart, yStart, radius, radius));
        }
    }

    @Override
    public boolean testPoint(int x, int y) {
        return Math.pow(getxCenter() - x, 2) + Math.pow(getyCenter() - y, 2) <= Math.pow(getRadius(), 2);
    }
}
