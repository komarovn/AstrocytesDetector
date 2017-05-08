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
package com.astrocytes.client.widgets.primitives;

public class SimpleRectangle extends AbstractPrimitive {

    private Integer xStart, yStart;
    private Integer xEnd, yEnd;

    public SimpleRectangle() { }

    public Integer getHeight() {
        return getBottomY() - getTopY();
    }

    public Integer getWidth() {
        return getRightX() - getLeftX();
    }

    public Integer getLeftX() {
        if (xStart == null || xEnd == null) return null;
        if (xEnd > xStart) {
            return xStart;
        }
        else {
            return xEnd;
        }
    }

    public Integer getTopY() {
        if (yStart == null || yEnd == null) return null;
        if (yEnd > yStart) {
            return yStart;
        }
        else {
            return yEnd;
        }
    }

    public Integer getRightX() {
        if (xStart == null || xEnd == null) return null;
        if (xEnd < xStart) {
            return xStart;
        }
        else {
            return xEnd;
        }
    }

    public Integer getBottomY() {
        if (yStart == null || yEnd == null) return null;
        if (yEnd < yStart) {
            return yStart;
        }
        else {
            return yEnd;
        }
    }

    public Integer getCenterX() {
        return getLeftX() + getWidth() / 2;
    }

    public Integer getCenterY() {
        return getTopY() + getHeight() / 2;
    }

    public void setStartPoint(Integer x, Integer y) {
        xStart = x;
        yStart = y;
    }

    public void setEndPoint(Integer x, Integer y) {
        xEnd = x;
        yEnd = y;
    }

    @Override
    public boolean isFull() {
        return xStart != null && yStart != null && xEnd != null && yEnd != null && getHeight() > 0 && getWidth() > 0;
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
}
