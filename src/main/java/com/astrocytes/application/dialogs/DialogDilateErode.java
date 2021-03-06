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
import com.astrocytes.application.resources.ApplicationConstants;
import com.astrocytes.application.widgets.instrument.InstrumentType;
import com.astrocytes.application.widgets.instrument.ZoomPanInstrument;
import com.astrocytes.core.ImageHelper;
import com.astrocytes.application.resources.StringResources;
import com.astrocytes.application.widgets.PreviewImageEditor;
import com.astrocytes.core.data.DataProvider;
import com.astrocytes.core.operationsengine.OperationsImpl;
import com.astrocytes.core.operationsengine.Operations;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

public class DialogDilateErode extends AbstractDialog {
    private final int RADIUS_DEFAULT = 2;

    private JSlider radiusSlider;
    private PreviewImageEditor preview;

    public DialogDilateErode(App owner, BufferedImage image) {
        super(owner.getFrame(), StringResources.DILATE_AND_ERODE);
        preview.addInstrument(new ZoomPanInstrument());
        preview.setImage(image);
        preview.processPreviewImage();
        preview.selectInstrument(InstrumentType.ZOOM_AND_PAN);
        setVisible(true);
    }

    @Override
    protected void initializeComponents() {
        add(new MainPanel(new DilateErodeBlock()));
    }

    private class DilateErodeBlock extends JPanel {
        private JFormattedTextField radiusTextbox;
        private JLabel radiusLabel;

        public DilateErodeBlock() {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;

            radiusLabel = new JLabel(StringResources.RADIUS_DILATE_ERODE);
            radiusTextbox = new JFormattedTextField(NumberFormat.getInstance());
            radiusTextbox.setValue(RADIUS_DEFAULT);
            radiusTextbox.setColumns(5);
            radiusSlider = new JSlider(1, 7, RADIUS_DEFAULT);

            preview = new PreviewImageEditor(ApplicationConstants.PREVIEW_WINDOW_WIDTH,
                    ApplicationConstants.PREVIEW_WINDOW_HEIGHT) {
                @Override
                public void processPreviewImage() {
                    processPreview();
                }
            };

            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets.bottom = 6;
            add(preview, gridBagConstraints);
            gridBagConstraints.insets.bottom = 0;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridy++;
            gridBagConstraints.insets.left = 4;
            gridBagConstraints.insets.bottom = 4;
            add(radiusLabel, gridBagConstraints);
            gridBagConstraints.gridx++;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.insets.left = 6;
            add(radiusTextbox, gridBagConstraints);
            gridBagConstraints.insets.left = 4;
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets.bottom = 6;
            add(radiusSlider, gridBagConstraints);

            addListeners(radiusSlider, radiusTextbox);
        }

        private void addListeners(final JSlider slider, final JFormattedTextField textField) {
            AbstractDialog.addListeners(slider, textField);

            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider state = (JSlider) e.getSource();
                    textField.setText(String.valueOf(state.getValue()));
                }
            });
            slider.addMouseListener(preview.getMouseAdapter());
        }
    }

    @Override
    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataProvider.setRadiusMathMorphology(getInstrumentRadius());
                setApplied(true);
                setVisible(false);
            }
        };
    }

    private int getInstrumentRadius() {
        return radiusSlider.getValue();
    }

    private void processPreview() {
        BufferedImage currentView = preview.getCurrentView();
        Operations operations = new OperationsImpl();
        operations.setSourceImage(ImageHelper.convertBufferedImageToMat(currentView));
        BufferedImage resultPreview = ImageHelper.convertMatToBufferedImage(
                operations.applyMathMorphology(getInstrumentRadius()));
        preview.updatePreview(resultPreview);
    }

}
