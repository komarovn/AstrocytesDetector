package com.astrocytes.dialogs;

import com.astrocytes.resources.StringResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nikolay Komarov on 04.03.2017.
 */
public class AbstractDialog extends JDialog {

    public AbstractDialog(JFrame owner, String title) {
        super(owner, title, true);
        initializeComponents();
        pack();
        setResizable(false);
        setVisible(true);
    }

    protected void initializeComponents() {
        add(new MainPanel(new JPanel()));
    }

    protected class MainPanel extends JPanel {
        private ActionPane actionPane;

        public MainPanel(JComponent contentBlock) {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = 0.33;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            add(contentBlock, gridBagConstraints);

            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx++;
            gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.weighty = 1;
            gridBagConstraints.weightx = 0;
            actionPane = new ActionPane();
            add(actionPane, gridBagConstraints);
        }
    }

    protected class ActionPane extends JPanel {

        private JButton ok;
        private JButton reset;

        public ActionPane() {
            ok = new JButton(StringResources.OK);
            reset = new JButton(StringResources.RESET);
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);

            add(ok, gridBagConstraints);
            gridBagConstraints.gridy++;
            add(reset, gridBagConstraints);
            addListeners();
        }

        private void addListeners() {
            initProceedAction();
            ok.addActionListener(proceedAction);
            reset.addActionListener(closeDialog);
        }

    }

    private ActionListener closeDialog = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    };

    protected ActionListener proceedAction;

    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Write specific implementation
            }
        };
    }
}
