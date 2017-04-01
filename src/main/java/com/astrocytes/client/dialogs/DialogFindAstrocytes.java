/*
 * Astrocytes Detector
 *
 * Designed by Komarov Nikolay.
 *
 * Copyright (c) Lobachevsky University, 2017.
 * All rights reserved.
 */
package com.astrocytes.client.dialogs;

import com.astrocytes.client.App;
import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.SwingJavaFXHelper;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import com.astrocytes.client.widgets.PreviewGraphicalWidget;
import com.astrocytes.server.OperationsImpl;
import com.astrocytes.shared.Operations;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class DialogFindAstrocytes extends AbstractDialog {
    private App owner;
    private PreviewGraphicalWidget preview;

    public DialogFindAstrocytes(App owner, BufferedImage image) {
        super(owner.getFrame(), StringResources.FIND_ASTROCYTES);
        this.owner = owner;
        preview.setImage(image);
        preview.setOriginalImage(ImageHelper.convertMatToBufferedImage(owner.getOperationsExecuter().getOperations().getSourceImage()));
        preview.processPreviewImage();
        setVisible(true);
    }

    @Override
    protected void initializeComponents() {
        add(new MainPanel(new FindAstrocytesBlock()));
    }

    private class FindAstrocytesBlock extends JPanel {

        public FindAstrocytesBlock() {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;

            preview = new PreviewGraphicalWidget(Integer.parseInt(ClientConstants.PREVIEW_WINDOW_WIDTH), Integer.parseInt(ClientConstants.PREVIEW_WINDOW_HEIGHT)) {
                @Override
                public void processPreviewImage() {
                    processPreview();
                }
            };

            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets.bottom = 6;
            add(preview, gridBagConstraints);

            gridBagConstraints.gridy++;

            JFXPanel content = new JFXPanel();
            add(content, gridBagConstraints);
            content.setPreferredSize(new Dimension(500, 600));
            SwingJavaFXHelper.initFX(content, getClass().getResource("/fxml/DialogFindAstrocytesContent.fxml"));
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

    private void processPreview() {
        BufferedImage currentView = preview.getCurrentView();
        Operations operations = new OperationsImpl();
        operations.setSourceImage(ImageHelper.convertBufferedImageToMat(preview.getOriginalImageView()));
        operations.drawAstrocyteCenters(ImageHelper.convertBufferedImageToMat(currentView));
        BufferedImage newCurrentView = ImageHelper.convertMatToBufferedImage(operations.getOutputImage()) ;
        preview.updatePreview(newCurrentView);
    }
}
