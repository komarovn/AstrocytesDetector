package com.astrocytes.client.dialogs;

import com.astrocytes.client.resources.StringResources;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
            initFX(content);
        }

        private void initFX(final JFXPanel content) {
            try {
                final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("DialogFindAstrocytesContent.fxml"));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Scene scene = new Scene(root);
                        content.setScene(scene);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
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
