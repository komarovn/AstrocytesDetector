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
package com.astrocytes.application.dialogs;

import com.astrocytes.application.App;
import com.astrocytes.application.widgets.instrument.DrawRectangleInstrument;
import com.astrocytes.application.widgets.instrument.ZoomPanInstrument;
import com.astrocytes.application.widgets.primitives.drawable.DrawingCircle;
import com.astrocytes.application.widgets.primitives.drawable.Paintable;
import com.astrocytes.core.ImageHelper;
import com.astrocytes.application.widgets.instrument.InstrumentType;
import com.astrocytes.application.resources.StringResources;
import com.astrocytes.application.widgets.PreviewImageEditor;
import com.astrocytes.application.widgets.primitives.SimpleRectangle;
import com.astrocytes.core.data.DataProvider;
import com.astrocytes.core.operationsengine.OperationsImpl;
import com.astrocytes.core.operationsengine.Operations;
import com.astrocytes.core.primitives.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DialogFindAstrocytes extends AbstractDialog {
    private App owner;
    private PreviewImageEditor preview;
    private SimpleRectangle boundingRectangle;
    private String boundingRectangleKey;
    private String astrocytesKey;

    public DialogFindAstrocytes(App owner, BufferedImage image) {
        super(owner.getFrame(), StringResources.FIND_ASTROCYTES);
        this.owner = owner;
        preview.addInstrument(new ZoomPanInstrument());
        preview.addInstrument(new DrawRectangleInstrument());
        preview.setImage(image);
        preview.setOriginalImage(ImageHelper.convertMatToBufferedImage(
                owner.getOperationsExecutor().getOperations().getSourceImage()));
        this.boundingRectangleKey = preview.getLayerManager().createLayer();
        this.astrocytesKey = preview.getLayerManager().createLayer();
        preview.processPreviewImage();
        preview.selectInstrument(InstrumentType.ZOOM_AND_PAN);
        setVisible(true);
    }

    @Override
    protected void initializeComponents() {
        add(new MainPanel(new FindAstrocytesBlock()));
    }

    private class FindAstrocytesBlock extends JPanel {
        private JButton drawRectangle;
        private JButton pan;

        public FindAstrocytesBlock() {
            ImageIcon iconPan = new ImageIcon(getClass().getResource("/img/pan.png"));
            ImageIcon iconRectangle = new ImageIcon(getClass().getResource("/img/rectangle.png"));

            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;

            preview = new PreviewImageEditor(380, 310) {
                @Override
                public void processPreviewImage() {
                    processPreview();
                }
            };

            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets.bottom = 6;
            gridBagConstraints.gridwidth = 2;
            add(preview, gridBagConstraints);

            gridBagConstraints.gridy++;
            gridBagConstraints.gridwidth = 1;
            drawRectangle = new JButton("", iconRectangle);
            drawRectangle.setMargin(new Insets(1, 1, 1, 1));
            drawRectangle.setFocusPainted(false);
            add(drawRectangle, gridBagConstraints);
            pan = new JButton("", iconPan);
            pan.setMargin(new Insets(1, 1, 1, 1));
            pan.setFocusPainted(false);
            gridBagConstraints.gridx++;
            add(pan, gridBagConstraints);

            attachActions();

            //JFXPanel content = new JFXPanel();
            //add(content, gridBagConstraints);
            //content.setPreferredSize(new Dimension(500, 600));
            //SwingJavaFXHelper.initFX(content, getClass().getResource("/fxml/DialogFindAstrocytesContent.fxml"));
        }

        private void attachActions() {
            drawRectangle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    preview.selectInstrument(InstrumentType.RECTANGLE);
                    ((DrawRectangleInstrument) preview.getActiveInstrument()).setDrawingKey(boundingRectangleKey);
                }
            });
            pan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    preview.selectInstrument(InstrumentType.ZOOM_AND_PAN);
                }
            });
        }
    }

    @Override
    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boundingRectangle.isFull()) {
                    DataProvider.setBoundingRectangleWidth(boundingRectangle.getWidth());
                    DataProvider.setBoundingRectangleHeight(boundingRectangle.getHeight());
                    DataProvider.setBoundingRectangleCenterX(boundingRectangle.getCenterX() + preview.getOffsetX());
                    DataProvider.setBoundingRectangleCenterY(boundingRectangle.getCenterY() + preview.getOffsetY());
                    setApplied(true);
                    setVisible(false);
                }
            }
        };
    }

    private void processPreview() {
        java.util.List<Paintable> rectangles = preview.getLayerManager().getLayer(this.boundingRectangleKey);
        if (rectangles.size() > 0) {
            boundingRectangle = (SimpleRectangle) rectangles.get(0);
        }
        Operations operations = new OperationsImpl();
        operations.setSourceImage(ImageHelper.convertBufferedImageToMat(preview.getOriginalImageView()));
        if (boundingRectangle != null && boundingRectangle.isFull()) {
            preview.getLayerManager().getLayer(astrocytesKey).clear();
            java.util.List<Point> centers = operations.findAstrocytes(boundingRectangle.getWidth(),
                    boundingRectangle.getHeight(),
                    boundingRectangle.getCenterX(),
                    boundingRectangle.getCenterY());
            java.util.List<DrawingCircle> astrocytes = new ArrayList<DrawingCircle>();

            for (Point center : centers) {
                astrocytes.add(new DrawingCircle((double) center.getX(), (double) center.getY(), 4.0));
            }

            preview.getLayerManager().getLayer(astrocytesKey).addAll(astrocytes);
        }
        else {
            preview.updatePreview(preview.getOriginalImageView());
        }
    }
}
