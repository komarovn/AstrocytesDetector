/*
 * Copyright (c) Lobachevsky University, 2018. All rights reserved.
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

import com.astrocytes.application.SwingJavaFXHelper;
import com.astrocytes.core.data.DataProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends AbstractController {

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ColorPicker astrocytesColorPicker;

    @FXML
    private ColorPicker neuronsColorPicker;

    @FXML
    private ColorPicker majorLayersColorPicker;

    @FXML
    private ColorPicker minorLayersColorPicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                applySettings();
                mainApp.executeSettings();
                closeAction();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeAction();
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                astrocytesColorPicker.setValue(SwingJavaFXHelper.convertAwtColor(DataProvider.getAstrocytesColor()));
                neuronsColorPicker.setValue(SwingJavaFXHelper.convertAwtColor(DataProvider.getNeuronsColor()));
                majorLayersColorPicker.setValue(SwingJavaFXHelper.convertAwtColor(DataProvider.getMajorLayersColor()));
                minorLayersColorPicker.setValue(SwingJavaFXHelper.convertAwtColor(DataProvider.getMinorLayersColor()));
            }
        });
    }

    private void applySettings() {
        DataProvider.setAstrocytesColor(SwingJavaFXHelper.convertFxColor(astrocytesColorPicker.getValue()));
        DataProvider.setNeuronsColor(SwingJavaFXHelper.convertFxColor(neuronsColorPicker.getValue()));
        DataProvider.setMajorLayersColor(SwingJavaFXHelper.convertFxColor(majorLayersColorPicker.getValue()));
        DataProvider.setMinorLayersColor(SwingJavaFXHelper.convertFxColor(minorLayersColorPicker.getValue()));
    }

    private void closeAction() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
