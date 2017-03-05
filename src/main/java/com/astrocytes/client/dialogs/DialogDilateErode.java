package com.astrocytes.client.dialogs;

import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import com.astrocytes.shared.AppParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class DialogDilateErode extends AbstractDialog {
    private final int RADIUS_DEFAULT = 2;

    public DialogDilateErode(JFrame owner) {
        super(owner, StringResources.DILATE_AND_ERODE);
    }

    @Override
    protected void initializeComponents() {
        add(new MainPanel(new DilateErodeBlock()));
    }

    private class DilateErodeBlock extends JPanel {
        private JFormattedTextField radiusTextbox;
        private JSlider radiusSlider;
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

            gridBagConstraints.anchor = GridBagConstraints.WEST;
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
        }
    }

    @Override
    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatus(true);
                setVisible(false);
            }
        };
    }

}
