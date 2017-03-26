package com.astrocytes.client.dialogs.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nikolay Komarov on 26.03.2017.
 */
public class CreateNewProjectController implements Initializable {

    @FXML
    private TextField projectName;

    @FXML
    private TextField imagePath;

    @FXML
    private Button openImageButton;

    @FXML
    private Button createProjectButton;

    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createProjectButton.setDisable(true);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        createProjectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        openImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        projectName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!projectName.getText().isEmpty()) {
                    createProjectButton.setDisable(false);
                }
                else {
                    createProjectButton.setDisable(true);
                }
            }
        });
        imagePath.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!imagePath.getText().isEmpty()) {
                    createProjectButton.setDisable(false);
                }
                else {
                    createProjectButton.setDisable(true);
                }
            }
        });
    }
}
