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
package com.astrocytes.client.dialogs;

import com.astrocytes.client.App;
import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.widgets.PreviewImageEditor;
import com.astrocytes.server.OperationsImpl;
import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import com.astrocytes.shared.Operations;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

public class DialogCannyEdgeDetection extends AbstractDialog {
    private final int MIN_THRESHOLD_DEFAULT = 40;
    private final int MAX_THRESHOLD_DEFAULT = 70;
    private JFormattedTextField minTextbox;
    private JFormattedTextField maxTextBox;
    private JSlider minThreshold;
    private JSlider maxThreshold;
    private PreviewImageEditor preview;

    public DialogCannyEdgeDetection(App owner, BufferedImage image) {
        super(owner.getFrame(), StringResources.CANNY_EDGE_DETECTION);
        preview.setImage(image);
        preview.processPreviewImage();
        setVisible(true);
    }

    @Override
    protected void initializeComponents() {
        add(new MainPanel(new CannyEdgeDetectionBlock()));
    }

    private class CannyEdgeDetectionBlock extends JPanel {
        private JLabel minThresholdLabel;
        private JLabel maxThresholdLabel;

        public CannyEdgeDetectionBlock() {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;

            minThreshold = new JSlider(0, 255, MIN_THRESHOLD_DEFAULT);
            maxThreshold = new JSlider(0, 255, MAX_THRESHOLD_DEFAULT);

            minTextbox = new JFormattedTextField(NumberFormat.getInstance());
            minTextbox.setValue(MIN_THRESHOLD_DEFAULT);
            minTextbox.setColumns(5);
            maxTextBox = new JFormattedTextField(NumberFormat.getInstance());
            maxTextBox.setValue(MAX_THRESHOLD_DEFAULT);
            maxTextBox.setColumns(5);

            minThresholdLabel = new JLabel(StringResources.MINIMUM_THRESHOLD);
            maxThresholdLabel =new JLabel(StringResources.MAXIMUM_THRESHOLD);

            preview = new PreviewImageEditor(Integer.parseInt(ClientConstants.PREVIEW_WINDOW_WIDTH), Integer.parseInt(ClientConstants.PREVIEW_WINDOW_HEIGHT)) {
                @Override
                public void processPreviewImage() {
                    processPreview();
                }
            };

            gridBagConstraints.insets.left = 4;
            gridBagConstraints.insets.bottom = 4;
            gridBagConstraints.gridwidth = 2;
            add(preview, gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridy++;
            add(minThresholdLabel, gridBagConstraints);
            gridBagConstraints.gridx++;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.insets.left = 6;
            add(minTextbox, gridBagConstraints);
            gridBagConstraints.insets.left = 4;
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets.bottom = 6;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            add(minThreshold, gridBagConstraints);

            gridBagConstraints.insets.bottom = 4;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy++;
            add(maxThresholdLabel, gridBagConstraints);
            gridBagConstraints.gridx++;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.insets.left = 6;
            add(maxTextBox, gridBagConstraints);
            gridBagConstraints.insets.left = 4;
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            add(maxThreshold, gridBagConstraints);

            addListeners(minThreshold, minTextbox, true);
            addListeners(maxThreshold, maxTextBox, false);
        }

        private void addListeners(final JSlider slider, final JFormattedTextField textField, final Boolean isMinimum) {
            AbstractDialog.addListeners(slider, textField);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider state = (JSlider) e.getSource();
                    if (isMinimum) {
                        if (state.getValue() > maxThreshold.getValue()) {
                            maxThreshold.setValue(state.getValue());
                        }
                        minTextbox.setText(String.valueOf(state.getValue()));
                    }
                    else {
                        if (state.getValue() < minThreshold.getValue()) {
                            minThreshold.setValue(state.getValue());
                        }
                        maxTextBox.setText(String.valueOf(state.getValue()));
                    }
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
                AppParameters.setParameter(ClientConstants.CANNY_MIN_THRESH, String.valueOf(getMinThresh()));
                AppParameters.setParameter(ClientConstants.CANNY_MAX_THRESH, String.valueOf(getMaxThresh()));
                setStatus(true);
                setVisible(false);
            }
        };
    }

    public int getMinThresh() {
        return minThreshold.getValue();
    }

    public int getMaxThresh() {
        return maxThreshold.getValue();
    }

    private void processPreview() {
        BufferedImage currentView = preview.getCurrentView();
        Operations operations = new OperationsImpl();
        operations.applyCannyEdgeDetection(ImageHelper.convertBufferedImageToMat(currentView), minThreshold.getValue(), maxThreshold.getValue());
        BufferedImage newCurrentView = ImageHelper.convertMatToBufferedImage(operations.getOutputImage()) ;
        preview.updatePreview(newCurrentView);
    }

}
