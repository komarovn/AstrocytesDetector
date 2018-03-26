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

import com.astrocytes.application.widgets.instrument.Instrument;
import com.astrocytes.application.widgets.instrument.InstrumentType;
import com.astrocytes.application.widgets.primitives.drawable.Paintable;
import com.astrocytes.application.widgets.primitives.SimpleLine;
import com.astrocytes.core.ImageHelper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class ImageEditor extends GraphicalWidget {

    private InstrumentType activeInstrument;
    private List<Instrument> instruments = new ArrayList<Instrument>();
    private ObjectManager objManager = new ObjectManager();

    public ImageEditor() {
        this(null, null);
    }

    public ImageEditor(Integer width, Integer height) {
        super(width, height);
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

    public void addInstrument(Instrument instrument) {
        instrument.setEditor(this);
        this.instruments.add(instrument);
    }

    public void removeInstrument(InstrumentType type) {
        for (Instrument instrument : instruments) {
            if (instrument.getType() == type) {
                this.instruments.remove(instrument);
                return;
            }
        }
    }

    public void selectInstrument(InstrumentType type) {
        for (Instrument instrument : instruments) {
            if (instrument.getType() == type) {
                this.activeInstrument = type;
                instrument.activate();
                updateWidget();
                return;
            }
        }
    }

    public Instrument getActiveInstrument() {
        for (Instrument instrument : instruments) {
            if (instrument.getType() == this.activeInstrument) {
                return instrument;
            }
        }
        return null;
    }

    public ObjectManager getObjectManager() {
        return this.objManager;
    }

    @Override
    public void reset() {
        super.reset();
        this.activeInstrument = InstrumentType.DEFAULT;
        this.objManager.clearAll();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Paintable obj : this.objManager.getAllPaintables()) {
            obj.paint((Graphics2D) g, getOffsetX(), getOffsetY(), getZoomValue());
        }
    }

    /**
     * Merge current layers with current image.
     * @return output image with all painted layers.
     */
    public BufferedImage renderImage() {
        if (getImage() != null) {
            BufferedImage result = ImageHelper.cloneBufferedImage(getImage());
            Graphics2D g = result.createGraphics();
            setupRenderHints(g);

            for (Paintable obj : this.objManager.getAllPaintables()) {
                obj.paint(g, 0, 0, 1.0);
            }
            return result;
        }
        return null;
    }

    //TODO: remove!
    public List<SimpleLine> getHorizontalLines() {
        Collections.sort(new ArrayList<Integer>(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return  o1 - o2;
            }
        });
        return null;
    }

    protected class ImageEditorListener extends GraphicalWidgetListener {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (currentView != null) {
                Instrument instrument = getActiveInstrument();
                if (instrument != null) {
                    instrument.onMouseDown(e);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (currentView != null) {
                Instrument instrument = getActiveInstrument();
                if (instrument != null) {
                    instrument.onMouseUp(e);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (currentView != null) {
                Instrument instrument = getActiveInstrument();
                if (instrument != null) {
                    instrument.onMouseDrag(e);
                }
            }
        }
    }

}
