package com.astrocytes.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nikolay Komarov on 04.03.2017.
 */
public class DialogCannyEdgeDetection extends JDialog {

    private JPanel mainPanel;

    public DialogCannyEdgeDetection(JFrame owner) {
        super(owner, "Canny Edge Detection", true);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        setSize(new Dimension(400, 200));
        setResizable(false);
        setVisible(true);
    }

}
