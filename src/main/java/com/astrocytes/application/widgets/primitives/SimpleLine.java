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
package com.astrocytes.application.widgets.primitives;

public class SimpleLine extends AbstractPrimitive {

    private Integer xStart, yStart;
    private Integer xEnd, yEnd;

    public SimpleLine() { }

    public SimpleLine(Integer xStart, Integer yStart, Integer xEnd, Integer yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    @Override
    public boolean isFull() {
        return xStart != null && yStart != null && xEnd != null && yEnd != null;
    }

    public void setStartPoint(Integer x, Integer y) {
        setxStart(x);
        setyStart(y);
    }

    public void setEndPoint(Integer x, Integer y) {
        setxEnd(x);
        setyEnd(y);
    }

    public Integer getxStart() {
        return xStart;
    }

    public void setxStart(Integer xStart) {
        this.xStart = xStart;
    }

    public Integer getyStart() {
        return yStart;
    }

    public void setyStart(Integer yStart) {
        this.yStart = yStart;
    }

    public Integer getxEnd() {
        return xEnd;
    }

    public void setxEnd(Integer xEnd) {
        this.xEnd = xEnd;
    }

    public Integer getyEnd() {
        return yEnd;
    }

    public void setyEnd(Integer yEnd) {
        this.yEnd = yEnd;
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
