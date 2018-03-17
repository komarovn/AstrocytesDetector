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

import com.astrocytes.application.widgets.primitives.AbstractPrimitive;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawingPolygonalChain extends AbstractPrimitive implements Paintable {
    private List<DrawingLine> chainParts;
    private Color objectColor;

    public DrawingPolygonalChain() {
        this(Color.BLUE);
    }

    public DrawingPolygonalChain(Color color) {
        this.chainParts = new ArrayList<DrawingLine>();
        this.objectColor = color;
    }

    public void addChainPart(DrawingLine part) {
        part.setColor(this.objectColor);
        this.chainParts.add(part);
    }

    @Override
    public void setColor(Color color) {
        this.objectColor = color;
        for (DrawingLine part : chainParts) {
            part.setColor(this.objectColor);
        }
    }

    @Override
    public boolean isFull() {
        for (DrawingLine part : chainParts) {
            if (!part.isFull()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void paint(Graphics2D graphics, int shiftX, int shiftY, double zoom) {
        for (DrawingLine part : chainParts) {
            part.paint(graphics, shiftX, shiftY, zoom);
        }
    }
}
