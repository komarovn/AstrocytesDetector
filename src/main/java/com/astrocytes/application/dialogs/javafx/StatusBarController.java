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
package com.astrocytes.application.dialogs.javafx;

import com.astrocytes.core.data.DataProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class StatusBarController extends AbstractController {
    private DataProvider dataProvider = new DataProvider();

    @FXML
    private Pane statusBar;

    @FXML
    private Label scaleLabel;

    @FXML
    private Pane scaleBar;

    @FXML
    private Label scaleBarLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setScaleValue(100);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainApp.getGraphicalWidget().addMouseWheelListener(new MouseAdapter() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setScaleValue((int) (mainApp.getGraphicalWidget().getZoomValue() * 100));
                                setScaleBarValue();
                            }
                        });
                    }
                });
            }
        });
    }

    private void setScaleValue(int scale) {
        scaleLabel.setText(String.valueOf(scale) + "%");
    }

    private void setScaleBarValue() {
        int scaleCoeff = dataProvider.getScale() * 2;
        scaleBar.setPrefWidth((int) (mainApp.getGraphicalWidget().getZoomValue() * scaleCoeff));
        scaleBarLabel.setText(mainApp.getGraphicalWidget().getZoomValue() * scaleCoeff + " μm");
    }

}
