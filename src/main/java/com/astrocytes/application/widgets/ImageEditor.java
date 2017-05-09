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

import com.astrocytes.application.InstrumentState;
import com.astrocytes.application.widgets.primitives.DrawingLine;
import com.astrocytes.application.widgets.primitives.SimpleLine;
import com.astrocytes.application.widgets.primitives.SimpleRectangle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class ImageEditor extends GraphicalWidget {

    private InstrumentState state;
    protected SimpleRectangle rectangle = new SimpleRectangle();
    protected java.util.List<DrawingLine> horizontalLines = new ArrayList<DrawingLine>();

    public ImageEditor() {
        this(null, null);
    }

    public ImageEditor(Integer width, Integer height) {
        super(width, height);
        state = InstrumentState.ZOOM_AND_PAN;
        addInstrumentListemers();
    }

    private void addInstrumentListemers() {
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
        updateWidget();
    }

    public SimpleRectangle getRectangle() {
        SimpleRectangle absoluteRectangle = new SimpleRectangle();
        if (rectangle.isFull()) {
            absoluteRectangle.setStartPoint(rectangle.getLeftX() + currentX, rectangle.getTopY() + currentY);
            absoluteRectangle.setEndPoint(rectangle.getRightX() + currentX, rectangle.getBottomY() + currentY);
        }
        return absoluteRectangle;
    }

    private void paintRectangle(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setPaint(Color.BLUE);
        graphics.setStroke(new BasicStroke(1));
        graphics.draw(new Rectangle2D.Float(rectangle.getLeftX(), rectangle.getTopY(),
                rectangle.getWidth(), rectangle.getHeight()));
    }

    private void paintHorizontalLines(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setPaint(Color.MAGENTA);
        graphics.setStroke(new BasicStroke(2));
        for (DrawingLine line : horizontalLines) {
            if (line.isFull()) {
                line.setxEnd(getImage().getWidth() < currentView.getWidth() ? Math.max(getImage().getWidth(), getWidth()) : currentView.getWidth());
                if (line.isDrawing()) {
                    graphics.setPaint(Color.ORANGE);
                }
                graphics.draw(new Line2D.Float(line.getxStart(), line.getyStart(), line.getxEnd(), line.getyEnd()));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rectangle.isFull()) {
            paintRectangle(g);
        }
        paintHorizontalLines(g);
    }

    public List<SimpleLine> getHorizontalLines() {
        List<SimpleLine> result = new ArrayList<SimpleLine>();
        Collections.sort(horizontalLines, new Comparator<DrawingLine>() {
            @Override
            public int compare(DrawingLine o1, DrawingLine o2) {
                return o1.getyEnd() - o2.getyEnd();
            }
        });
        for (DrawingLine line : horizontalLines) {
            DrawingLine absoluteLine = new DrawingLine(line.getxStart(), line.getyStart() + currentY, line.getxEnd(), line.getyEnd() + currentY);
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
                        lockZoomAndPan();
                        break;
                    case ZOOM_AND_PAN:
                        unlockZoomAndPan();
                        super.mousePressed(e);
                        break;
                    case RECTANGLE:
                        super.mousePressed(e);
                        lockZoomAndPan();
                        rectangle.setStartPoint(e.getX(), e.getY());
                        isDrawing = true;
                        break;
                    case LINE_HORIZONTAL:
                        super.mousePressed(e);
                        lockZoomAndPan();
                        creationLine = new DrawingLine();
                        creationLine.setStartPoint(0, e.getY());
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
                        moveObjects();
                        break;
                    case RECTANGLE:
                        rectangle.setEndPoint(e.getX(), e.getY());
                        repaint();
                        isDrawing = false;
                        break;
                    case LINE_HORIZONTAL:
                        horizontalLines.remove(creationLine);
                        creationLine.setStartPoint(0, e.getY());
                        creationLine.setEndPoint(currentView.getWidth(), e.getY());
                        creationLine.setDrawing(false);
                        horizontalLines.add(creationLine);
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
                        moveObjects();
                        break;
                    case RECTANGLE:
                        if (isDrawing) {
                            rectangle.setEndPoint(e.getX(), e.getY());
                            repaint();
                        }
                        break;
                    case LINE_HORIZONTAL:
                        if (isDrawing) {
                            horizontalLines.remove(creationLine);
                            creationLine.setStartPoint(0, e.getY());
                            creationLine.setEndPoint(currentView.getWidth(), e.getY());
                            horizontalLines.add(creationLine);
                            repaint();
                        }
                        break;
                }
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            if (currentView != null) {
                switch (state) {
                    case ZOOM_AND_PAN:
                        zoomObjects();
                        break;
                }
            }
        }

        private void moveObjects() {
            int dX = currentX > 0 && currentView.getWidth() + currentX < getImage().getWidth() * zoomScale ? -deltaX : 0;
            int dY = currentY > 0 && currentView.getHeight() + currentY < getImage().getHeight() * zoomScale ? -deltaY : 0;
            if (rectangle.isFull()) {
                rectangle.move(dX, dY);
            }
            for (DrawingLine line : horizontalLines) {
                if (line.isFull()) {
                    line.move(0, dY);
                }
            }
        }

        private void zoomObjects() {
            for (DrawingLine line : horizontalLines) {
                if (line.isFull()) {
                    int yNew = (int) (isZoomIn ? 2 : 0.5 * (line.getyEnd() + currentYOld)) - currentY;
                    line.setyEnd(yNew);
                    line.setyStart(yNew);
                }
            }
        }
    }

}
