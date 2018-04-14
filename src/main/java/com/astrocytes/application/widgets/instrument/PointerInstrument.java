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

import com.astrocytes.application.resources.ApplicationConstants;
import com.astrocytes.application.widgets.primitives.drawable.Paintable;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PointerInstrument extends Instrument {
    private Paintable selectedObj;
    private Color selectedObjColor;

    @Override
    public InstrumentType getType() {
        return InstrumentType.POINTER;
    }

    @Override
    public void activate() {
        getEditor().lockZoomAndPan();
    }

    @Override
    public void onMouseDown(MouseEvent e) {
        int globX = (int) ((e.getX() + getEditor().getOffsetX()) / getEditor().getZoomValue());
        int globY = (int) ((e.getY() + getEditor().getOffsetY()) / getEditor().getZoomValue());

        revertSelection();

        for (Paintable obj : getEditor().getObjectManager().getAllPaintables()) {
            if (obj.testPoint(globX, globY)) {
                selectedObj = obj;
                break;
            }
        }

        select();
    }

    @Override
    public void onMouseDrag(MouseEvent e) {

    }

    @Override
    public void onMouseUp(MouseEvent e) {

    }

    private void select() {
        if (selectedObj != null) {
            selectedObjColor = selectedObj.getColor();
            selectedObj.setColor(ApplicationConstants.SELECTION_COLOR);
            getEditor().updateWidget();
        }
    }

    private void revertSelection() {
        if (selectedObj != null && selectedObjColor != null) {
            selectedObj.setColor(selectedObjColor);
            getEditor().updateWidget();
        }

        selectedObj = null;
        selectedObjColor = null;
    }
}
