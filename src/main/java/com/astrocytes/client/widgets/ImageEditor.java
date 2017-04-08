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
package com.astrocytes.client.widgets;

import com.astrocytes.client.InstrumentState;
import com.astrocytes.client.widgets.primitives.SimpleRectangle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class ImageEditor extends GraphicalWidget {

    private InstrumentState state;
    private SimpleRectangle rectangle = new SimpleRectangle();

    public ImageEditor(Integer width, Integer height) {
        super(width, height);
        state = InstrumentState.ZOOM_AND_PAN;
        addInstrumentListemers();
    }

    private void addInstrumentListemers() {
        removeMouseListener(getMouseListeners()[0]);
        removeMouseMotionListener(getMouseMotionListeners()[0]);
        ImageEditorListener listener = new ImageEditorListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public InstrumentState getState() {
        return state;
    }

    public void setState(InstrumentState state) {
        this.state = state;
    }

    private void paintRectangle() {
        Graphics2D graphics = (Graphics2D) getGraphics();
        graphics.setPaint(Color.BLUE);
        graphics.setStroke(new BasicStroke(1));
        graphics.draw(new Rectangle2D.Float(rectangle.getLeftX(), rectangle.getTopY(), rectangle.getWidth(), rectangle.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state.equals(InstrumentState.RECTANGLE)) {
            paintRectangle();
        }
    }

    protected class ImageEditorListener extends GraphicalWidgetListener {
        private boolean isDrawing = false;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (state.equals(InstrumentState.RECTANGLE)) {
                lockZoomAndPan();
                rectangle.setStartPoint(e.getX(), e.getY());
                isDrawing = true;
            }
            if (state.equals(InstrumentState.ZOOM_AND_PAN)) {
                unlockZoomAndPan();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (state.equals(InstrumentState.RECTANGLE)) {
                rectangle.setEndPoint(e.getX(), e.getY());
                repaint();
                paintRectangle();
                isDrawing = false;
            }
            if (state.equals(InstrumentState.ZOOM_AND_PAN)) {

            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (state.equals(InstrumentState.RECTANGLE) && isDrawing) {
                rectangle.setEndPoint(e.getX(), e.getY());
                repaint();
                paintRectangle();
            }
            if (state.equals(InstrumentState.ZOOM_AND_PAN)) {

            }
        }
    }

}
