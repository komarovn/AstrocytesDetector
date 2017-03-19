package com.astrocytes.client.dialogs;

import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.resources.StringResources;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class DialogFindAstrocytes extends AbstractDialog {

    public DialogFindAstrocytes(JFrame owner) {
        super(owner, StringResources.FIND_ASTROCYTES);
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

            // TODO: add to JPanel fxml config of gui via JFXPanel
            JFXPanel content = new JFXPanel();
            add(content);
            content.setPreferredSize(new Dimension(500, 600));
            ImageHelper.initFX(content, getClass().getResource("/fxml/DialogFindAstrocytesContent.fxml"));
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
