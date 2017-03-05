package com.astrocytes.dialogs;

import com.astrocytes.resources.StringResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nikolay Komarov on 04.03.2017.
 */
public class DialogCannyEdgeDetection extends AbstractDialog {
    private final int MIN_THRESHOLD_DEFAULT = 40;
    private final int MAX_THRESHOLD_DEFAULT = 70;
    private JTextField minTextbox;
    private JTextField maxTextBox;

    public DialogCannyEdgeDetection(JFrame owner) {
        super(owner, StringResources.CANNY_EDGE_DETECTION);
    }

    @Override
    protected void initializeComponents() {
        add(new MainPanel(new CannyEdgeDetectionBlock()));
    }

    private class CannyEdgeDetectionBlock extends JPanel {
        private JSlider minThreshold;
        private JSlider maxThreshold;
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
            minTextbox = new JTextField(String.valueOf(MIN_THRESHOLD_DEFAULT), 5);
            maxTextBox = new JTextField(String.valueOf(MAX_THRESHOLD_DEFAULT), 5);
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

            addListeners();
        }

        private void addListeners() {
            
        }
    }

    @Override
    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: write action
                int minThreshold = Integer.valueOf(minTextbox.getText());
                int maxThreshold = Integer.valueOf(maxTextBox.getText());
                setVisible(false);
            }
        };
    }
}
