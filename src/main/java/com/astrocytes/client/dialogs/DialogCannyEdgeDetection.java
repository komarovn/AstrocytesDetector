package com.astrocytes.client.dialogs;

import com.astrocytes.shared.AppParameters;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

/**
 * Created by Nikolay Komarov on 04.03.2017.
 */
public class DialogCannyEdgeDetection extends AbstractDialog {
    private final int MIN_THRESHOLD_DEFAULT = 40;
    private final int MAX_THRESHOLD_DEFAULT = 70;
    private JFormattedTextField minTextbox;
    private JFormattedTextField maxTextBox;
    private JSlider minThreshold;
    private JSlider maxThreshold;

    public DialogCannyEdgeDetection(JFrame owner) {
        super(owner, StringResources.CANNY_EDGE_DETECTION);
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

            gridBagConstraints.insets.left = 4;
            gridBagConstraints.insets.bottom = 4;
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
            add(minThreshold, gridBagConstraints);

            gridBagConstraints.insets.bottom = 4;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy++;
            add(maxThresholdLabel, gridBagConstraints);
            gridBagConstraints.gridx++;
            gridBagConstraints.insets.left = 6;
            add(maxTextBox, gridBagConstraints);
            gridBagConstraints.insets.left = 4;
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 2;
            add(maxThreshold, gridBagConstraints);

            addListeners(minThreshold, minTextbox, true);
            addListeners(maxThreshold, maxTextBox, false);
        }

        private void addListeners(final JSlider slider, final JFormattedTextField textField, final Boolean isMinimum) {
            KeyListener keyListener = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {

                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (!textField.getValue().equals("")) {
                        slider.setValue(Integer.parseInt(textField.getText()));
                    }
                    else {
                        textField.setValue(0);
                    }
                }
            };
            textField.addKeyListener(keyListener);

            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider state = (JSlider) e.getSource();
                    if (isMinimum) {
                        if (state.getValue() > maxThreshold.getValue()) {
                            maxThreshold.setValue(state.getValue());
                        }
                        minTextbox.setValue(state.getValue());
                    }
                    else {
                        if (state.getValue() < minThreshold.getValue()) {
                            minThreshold.setValue(state.getValue());
                        }
                        maxTextBox.setValue(state.getValue());
                    }

                }
            });
        }

    }

    @Override
    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppParameters.setParameter(ClientConstants.CANNY_MIN_THRESH, getMinThresh());
                AppParameters.setParameter(ClientConstants.CANNY_MAX_THRESH, getMaxThresh());
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
}
