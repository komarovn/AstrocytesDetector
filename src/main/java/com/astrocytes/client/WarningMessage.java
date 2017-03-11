package com.astrocytes.client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nikolay Komarov on 10.03.2017.
 */
public class WarningMessage extends JDialog {

    private WarningMessagePopup popup;
    private String message;

    public WarningMessage(JFrame owner, String message) {
        super(owner, "", true);
        this.message = message;
        popup = new WarningMessagePopup();
        add(popup);
        pack();
        setVisible(true);
    }

    private class WarningMessagePopup extends JPanel {

        public WarningMessagePopup() {
            JPanel main = new JPanel();
            main.setPreferredSize(new Dimension(200, 70));
            add(main);
        }
    }

}
