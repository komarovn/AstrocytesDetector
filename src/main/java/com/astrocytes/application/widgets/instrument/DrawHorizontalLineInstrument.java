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

import com.astrocytes.application.widgets.primitives.drawable.DrawingLine;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawHorizontalLineInstrument extends Instrument {
    private boolean isDrawingState = false;
    private DrawingLine line;
    private String linesKey;

    @Override
    public InstrumentType getType() {
        return InstrumentType.LINE_HORIZONTAL;
    }

    @Override
    public void activate() {
        getEditor().lockZoomAndPan();
    }

    @Override
    public void onMouseDown(MouseEvent e) {
        this.line = new DrawingLine(Color.ORANGE);
        updateLine(e.getX(), e.getY());
        getEditor().getObjectManager().getGroup(getLinesKey()).add(this.line);
        getEditor().repaint();
        this.isDrawingState = true;
    }

    @Override
    public void onMouseDrag(MouseEvent e) {
        if (isDrawingState) {
            updateLine(e.getX(), e.getY());
            getEditor().repaint();
        }
    }

    @Override
    public void onMouseUp(MouseEvent e) {
        updateLine(e.getX(), e.getY());
        this.line.setColor(Color.BLUE);
        getEditor().repaint();
        this.isDrawingState = false;
        this.line = null;
    }

    private void updateLine(int x, int y) {
        if (this.line != null && getEditor().testPoint(x, y)) {
            this.line.setStartPoint(0.0, (y + getEditor().getOffsetY()) / getEditor().getZoomValue());
            this.line.setEndPoint((double) getEditor().getImage().getWidth(),
                    (y + getEditor().getOffsetY()) / getEditor().getZoomValue());
        }
    }

    private String getLinesKey() {
        if (this.linesKey == null) {
            this.linesKey = getEditor().getObjectManager().createGroup();
        }
        return this.linesKey;
    }
}
