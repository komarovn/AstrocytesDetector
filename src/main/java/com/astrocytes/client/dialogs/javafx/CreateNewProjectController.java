package com.astrocytes.client.dialogs.javafx;

import com.astrocytes.client.App;
import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.resources.ClientConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

/**
 * Created by Nikolay Komarov on 26.03.2017.
 */
public class CreateNewProjectController implements Initializable {

    private App mainApp;

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
        createProjectButton.setDisable(true);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeAction();
            }
        });
        createProjectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AppParameters.setSetting(ClientConstants.PROJECT_NAME, projectName.getText());
                AppParameters.setParameter(ClientConstants.SCALE, scale.getText());
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainApp.executeCreateNewProject(new File(imagePath.getText()));
                    }
                });
                closeAction();
            }
        });
        openImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openImageAction();
            }
        });
        projectName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                checkMandatoryFields();
            }
        });
        imagePath.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                checkMandatoryFields();
            }
        });
        imagePath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkMandatoryFields();
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
        scale.setText("10");
        scale.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                checkMandatoryFields();
            }
        });
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
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
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void checkMandatoryFields() {
        if (!imagePath.getText().isEmpty() &&
                !projectName.getText().isEmpty() &&
                !scale.getText().isEmpty()) {
            createProjectButton.setDisable(false);
        }
        else {
            createProjectButton.setDisable(true);
        }
    }
}
