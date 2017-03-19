package com.astrocytes.client.dialogs.javafx;

import com.astrocytes.client.App;
import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nikolay Komarov on 16.03.2017.
 */
public class AppController implements Initializable {

    private App mainApp;
    private Stage ownerStage;

    @FXML
    private javafx.scene.control.MenuBar menuBar;

    @FXML
    private MenuItem createNewProject;

    @FXML
    private MenuItem openProject;

    @FXML
    private MenuItem saveProjectAs;

    @FXML
    private MenuItem exportImage;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem cannyEdDet;

    @FXML
    private MenuItem dilErode;

    @FXML
    private MenuItem grayscale;

    @FXML
    private MenuItem findAstrocytes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* --- File --- */
        createNewProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainApp.executeCreateNewProject();
                    }
                });
            }
        });
        saveProjectAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveProjectAsAction();
            }
        });
        exportImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainApp.executeExportImage();
                    }
                });
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeExit();
            }
        });

        /* --- Operations --- */
        cannyEdDet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeEdgeDetection();
            }
        });
        dilErode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeDilateErode();
            }
        });
        grayscale.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeGrayscale();
            }
        });
        findAstrocytes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeFindAstrocytes();
            }
        });
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    public void setAvailability(boolean isEmpty) {
        saveProjectAs.setDisable(isEmpty);
        exportImage.setDisable(isEmpty);
        cannyEdDet.setDisable(isEmpty);
        dilErode.setDisable(isEmpty);
        grayscale.setDisable(isEmpty);
        findAstrocytes.setDisable(isEmpty);
    }

    private void saveProjectAsAction() {
        DirectoryChooser saveProjectDialog = new DirectoryChooser();
        saveProjectDialog.setTitle(StringResources.SAVE_PROJECT_AS);
        File selectedDirectory = saveProjectDialog.showDialog(menuBar.getScene().getWindow());
        if (selectedDirectory != null) {
            saveProject(selectedDirectory);
        }
    }

    public void saveProject(File selectedDirectory) {
        try {
            File projectDir = new File(selectedDirectory, "Project Name");
            projectDir.mkdir();
            AppParameters.setSetting(ClientConstants.PROJECT_DIR, projectDir);
            File settings = new File(projectDir, "settings.xml");
            settings.createNewFile();
            File parameters = new File(projectDir, "parameters.xml");
            parameters.mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
