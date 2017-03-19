package com.astrocytes.client.dialogs;

import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import com.astrocytes.client.widgets.PreviewGraphicalWidget;
import com.astrocytes.server.OperationsImpl;
import com.astrocytes.client.data.AppParameters;
import com.astrocytes.shared.Operations;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class DialogDilateErode extends AbstractDialog {
    private final int RADIUS_DEFAULT = 2;
    private JSlider radiusSlider;
    private PreviewGraphicalWidget preview;

    public DialogDilateErode(JFrame owner, BufferedImage image) {
        super(owner, StringResources.DILATE_AND_ERODE);
        preview.setImage(image);
        preview.processPreviewImage();
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

            preview = new PreviewGraphicalWidget(ClientConstants.PREVIEW_WINDOW_WIDTH, ClientConstants.PREVIEW_WINDOW_HEIGHT) {
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
                AppParameters.setParameter(ClientConstants.RADIUS_DIL_ER, getInstrumentRadius());
                setStatus(true);
                setVisible(false);
            }
        };
    }

    public int getInstrumentRadius() {
        return radiusSlider.getValue();
    }

    private void processPreview() {
        BufferedImage currentView = preview.getCurrentView();
        Operations operations = new OperationsImpl();
        operations.applyMathMorphology(ImageHelper.convertBufferedImageToMat(currentView), radiusSlider.getValue());
        BufferedImage newCurrentView = ImageHelper.convertMatToBufferedImage(operations.getOutputImage()) ;
        preview.updatePreview(newCurrentView);
    }

}
