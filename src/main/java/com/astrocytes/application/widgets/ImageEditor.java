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
package com.astrocytes.application.widgets;

import com.astrocytes.application.widgets.primitives.drawable.DrawingLine;
import com.astrocytes.application.widgets.primitives.drawable.Paintable;
import com.astrocytes.application.widgets.primitives.SimpleLine;
import com.astrocytes.application.widgets.primitives.SimpleRectangle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ImageEditor extends GraphicalWidget {

    private InstrumentState state;

    //TODO: delete
    protected SimpleRectangle rectangle = new SimpleRectangle();
    protected java.util.List<DrawingLine> horizontalLines = new ArrayList<DrawingLine>();

    protected List<Paintable> paintableObjects = new ArrayList<Paintable>();

    public ImageEditor() {
        this(null, null);
    }

    public ImageEditor(Integer width, Integer height) {
        super(width, height);
        state = InstrumentState.ZOOM_AND_PAN;
        initInstrumentListeners();
    }

    private void initInstrumentListeners() {
        removeMouseListener(getMouseListeners()[0]);
        removeMouseMotionListener(getMouseMotionListeners()[0]);
        removeMouseWheelListener(getMouseWheelListeners()[0]);
        ImageEditorListener listener = new ImageEditorListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
    }

    public InstrumentState getState() {
        return state;
    }

    public void setState(InstrumentState state) {
        this.state = state;
        switch (state) {
            case ZOOM_AND_PAN:
                unlockZoomAndPan();
                break;
            default:
                lockZoomAndPan();
        }
        updateWidget();
    }

    public List<Paintable> getPaintableObjects() {
        return paintableObjects;
    }

    @Override
    public void reset() {
        super.reset();
        state = InstrumentState.ZOOM_AND_PAN;
        paintableObjects.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Paintable obj : paintableObjects) {
            obj.paint((Graphics2D) g, getOffsetX(), getOffsetY(), getZoomValue());
        }
    }

    public List<SimpleLine> getHorizontalLines() {
        List<SimpleLine> result = new ArrayList<SimpleLine>();
        Collections.sort(horizontalLines, new Comparator<DrawingLine>() {
            @Override
            public int compare(DrawingLine o1, DrawingLine o2) {
                return o1.getyEnd().intValue() - o2.getyEnd().intValue();
            }
        });
        for (DrawingLine line : horizontalLines) {
            SimpleLine absoluteLine = new SimpleLine(line.getxStart(), 1 / getZoomValue() * (line.getyStart() + getOffsetY()),
                    line.getxEnd(), 1 / getZoomValue() * (line.getyEnd() + getOffsetY()));
            result.add(absoluteLine);
        }
        return result;
    }

    protected class ImageEditorListener extends GraphicalWidgetListener {
        private boolean isDrawing = false;
        private DrawingLine creationLine;

        @Override
        public void mousePressed(MouseEvent e) {
            if (currentView != null) {
                switch (state) {
                    case POINTER:
                        break;
                    case ZOOM_AND_PAN:
                        super.mousePressed(e);
                        break;
                    case RECTANGLE:
                        super.mousePressed(e);
                        rectangle.setStartPoint(e.getX(), e.getY());
                        isDrawing = true;
                        break;
                    case LINE_HORIZONTAL:
                        super.mousePressed(e);
                        creationLine = new DrawingLine();
                        creationLine.setStartPoint(0.0, (e.getY() + getOffsetY()) / getZoomValue());
                        creationLine.setEndPoint((double) getImage().getWidth(), (e.getY() + getOffsetY()) / getZoomValue());
                        repaint();
                        isDrawing = true;
                        break;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (currentView != null) {
                switch (state) {
                    case POINTER:
                        break;
                    case ZOOM_AND_PAN:
                        break;
                    case RECTANGLE:
                        rectangle.setEndPoint(e.getX(), e.getY());
                        repaint();
                        isDrawing = false;
                        break;
                    case LINE_HORIZONTAL:
                        paintableObjects.remove(creationLine);
                        if (isInOfImageBoundary(e.getX(), e.getY())) {
                            creationLine.setStartPoint(0.0, (e.getY() + getOffsetY()) / getZoomValue());
                            creationLine.setEndPoint((double) getImage().getWidth(), (e.getY() + getOffsetY()) / getZoomValue());
                        }
                        creationLine.setDrawing(false);
                        paintableObjects.add(creationLine);
                        repaint();
                        isDrawing = false;
                        break;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (currentView != null) {
                switch (state) {
                    case POINTER:
                        break;
                    case ZOOM_AND_PAN:
                        break;
                    case RECTANGLE:
                        if (isDrawing && isInOfImageBoundary(e.getX(), e.getY())) {
                            rectangle.setEndPoint(e.getX(), e.getY());
                            repaint();
                        }
                        break;
                    case LINE_HORIZONTAL:
                        if (isDrawing && isInOfImageBoundary(e.getX(), e.getY())) {
                            paintableObjects.remove(creationLine);
                            creationLine.setStartPoint(0.0, (e.getY() + getOffsetY()) / getZoomValue());
                            creationLine.setEndPoint((double) getImage().getWidth(), (e.getY() + getOffsetY()) / getZoomValue());
                            paintableObjects.add(creationLine);
                            repaint();
                        }
                        break;
                }
            }
        }

        private boolean isInOfImageBoundary(int x, int y) {
            return y <= getZoomedImage().getHeight() && x <= getZoomedImage().getWidth();
        }
    }

}
