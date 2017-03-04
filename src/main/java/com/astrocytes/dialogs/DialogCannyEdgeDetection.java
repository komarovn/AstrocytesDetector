package com.astrocytes.dialogs;

import com.astrocytes.resources.StringResources;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nikolay Komarov on 04.03.2017.
 */
public class DialogCannyEdgeDetection extends JDialog {

    private JPanel mainPanel;

    public DialogCannyEdgeDetection(JFrame owner) {
        super(owner, StringResources.CANNY_EDGE_DETECTION, true);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        setSize(new Dimension(400, 200));
        setResizable(false);
        setVisible(true);
    }

}
