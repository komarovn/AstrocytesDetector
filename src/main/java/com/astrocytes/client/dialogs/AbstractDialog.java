/*
 * Copyright (c) Lobachevsky University, 2017. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal with the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * Developed by: Komarov Nikolay.
 */
package com.astrocytes.client.dialogs;

import com.astrocytes.client.resources.StringResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AbstractDialog extends JDialog {
    private Boolean status = false; // ok (true) or reset (false) button was pressed

    public AbstractDialog(JFrame owner, String title) {
        super(owner, title, true);
        initializeComponents();
        pack();
        setResizable(false);
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
            gridBagConstraints.insets = new Insets(4, 0, 4, 6);
            add(contentBlock, gridBagConstraints);

            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx++;
            gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
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
            gridBagConstraints.insets = new Insets(4, 2, 4, 4);
            gridBagConstraints.anchor = GridBagConstraints.NORTH;

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
            status = false;
            setVisible(false);
        }
    };

    protected ActionListener proceedAction;

    protected void initProceedAction() {
        proceedAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = true;
            }
        };
    }

    protected void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    protected static void addListeners(final JSlider slider, final JFormattedTextField textField) {
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!textField.getText().equals("")) {
                    slider.setValue(Integer.parseInt(textField.getText()));
                }
                else {
                    slider.setValue(slider.getMinimum());
                }
            }
        };
        textField.addKeyListener(keyListener);
    }
}
