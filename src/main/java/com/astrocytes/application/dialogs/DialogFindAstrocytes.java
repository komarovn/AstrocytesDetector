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
import com.astrocytes.application.ImageHelper;
import com.astrocytes.application.InstrumentState;
import com.astrocytes.core.data.AppParameters;
import com.astrocytes.core.CoreConstants;
import com.astrocytes.application.resources.StringResources;
import com.astrocytes.application.widgets.PreviewImageEditor;
import com.astrocytes.application.widgets.primitives.SimpleRectangle;
import com.astrocytes.core.OperationsImpl;
import com.astrocytes.shared.Operations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class DialogFindAstrocytes extends AbstractDialog {
    private App owner;
    private PreviewImageEditor preview;
    private InstrumentState state;
    private SimpleRectangle boundingRectangle;

    public DialogFindAstrocytes(App owner, BufferedImage image) {
        super(owner.getFrame(), StringResources.FIND_ASTROCYTES);
        this.owner = owner;
        preview.setImage(image);
        preview.setOriginalImage(ImageHelper.convertMatToBufferedImage(
                owner.getOperationsExecuter().getOperations().getSourceImage()));
        preview.processPreviewImage();
        state = InstrumentState.ZOOM_AND_PAN;
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
            drawRectangle = new JButton(StringResources.DRAW_RECTANGLE);
            add(drawRectangle, gridBagConstraints);
            pan = new JButton(StringResources.PAN);
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
                    state = InstrumentState.RECTANGLE;
                    preview.setState(state);
                }
            });
            pan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = InstrumentState.ZOOM_AND_PAN;
                    preview.setState(state);
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
                    AppParameters.setParameter(CoreConstants.BOUNDING_RECTANGLE_WIDTH,
                            String.valueOf(boundingRectangle.getWidth()));
                    AppParameters.setParameter(CoreConstants.BOUNDING_RECTANGLE_HEIGHT,
                            String.valueOf(boundingRectangle.getHeight()));
                    AppParameters.setParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_X,
                            String.valueOf(boundingRectangle.getCenterX()  + preview.getOffsetX()));
                    AppParameters.setParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_Y,
                            String.valueOf(boundingRectangle.getCenterY() + preview.getOffsetY()));
                    setApplied(true);
                    setVisible(false);
                }
            }
        };
    }

    private void processPreview() {
        BufferedImage currentView = preview.getCurrentView();
        boundingRectangle = preview.getRectangle();
        Operations operations = new OperationsImpl();
        operations.setSourceImage(ImageHelper.convertBufferedImageToMat(preview.getOriginalImageView()));
        if (boundingRectangle.isFull()) {
            operations.findAstrocytes(ImageHelper.convertBufferedImageToMat(currentView),
                    boundingRectangle.getWidth(),
                    boundingRectangle.getHeight(),
                    boundingRectangle.getCenterX(),
                    boundingRectangle.getCenterY());
            BufferedImage newCurrentView = ImageHelper.convertMatToBufferedImage(operations.getOutputImage()) ;
            preview.updatePreview(newCurrentView);
        }
        else {
            preview.updatePreview(preview.getOriginalImageView());
        }
    }
}