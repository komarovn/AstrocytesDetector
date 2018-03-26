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

import com.astrocytes.application.resources.ApplicationConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateNewProjectController extends AbstractController {

    @FXML
    private TextField projectName;

    @FXML
    private TextField imagePath;

    @FXML
    private TextField scale;

    @FXML
    private Button openImageButton;

    @FXML
    private Button createProjectButton;

    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCreateButton();
        initCancelButton();
        initOpenButton();
        initProjectNameField();
        initImgPathField();
        initScaleField();
    }

    private void initCreateButton() {
        createProjectButton.setDisable(true);
        createProjectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainApp.executeCreateNewProject(projectName.getText(),
                                new File(imagePath.getText()),
                                Integer.valueOf(scale.getText()));
                    }
                });
                closeAction();
            }
        });
    }

    private void initCancelButton() {
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeAction();
            }
        });
    }

    private void initOpenButton() {
        openImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openImageAction();
            }
        });
    }

    private void initProjectNameField() {
        projectName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                validateMandatoryFields();
            }
        });
    }

    private void initImgPathField() {
        imagePath.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                validateMandatoryFields();
            }
        });
        imagePath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateMandatoryFields();
            }
        });
        imagePath.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (imagePath.getText().isEmpty()) {
                    openImageAction();
                }
            }
        });
    }

    private void initScaleField() {
        scale.setText(String.valueOf(ApplicationConstants.DEFAULT_SCALE));
        scale.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                validateMandatoryFields();
            }
        });
    }

    private void openImageAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Image");
        File image = fileChooser.showOpenDialog(cancelButton.getScene().getWindow());
        if (image != null) {
            imagePath.setText(image.getAbsolutePath());
        }
    }

    private void closeAction() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void validateMandatoryFields() {
        createProjectButton.setDisable(imagePath.getText().isEmpty() ||
                projectName.getText().isEmpty() ||
                scale.getText().isEmpty());
    }
}
