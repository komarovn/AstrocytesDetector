package com.astrocytes.client.dialogs.javafx;

import com.astrocytes.client.App;
import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.data.ManageProject;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nikolay Komarov on 16.03.2017.
 */
public class MenuController implements Initializable {

    private App mainApp;

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
                createNewProjectAction();
            }
        });
        openProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadProjectAction();
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

    public void setAvailability(boolean isEmpty) {
        saveProjectAs.setDisable(isEmpty);
        exportImage.setDisable(isEmpty);
        cannyEdDet.setDisable(isEmpty);
        dilErode.setDisable(isEmpty);
        grayscale.setDisable(isEmpty);
        findAstrocytes.setDisable(isEmpty);
    }

    private void createNewProjectAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CreateNewProject.fxml"));
            Parent root = fxmlLoader.load();
            CreateNewProjectController controller = fxmlLoader.getController();
            controller.setMainApp(mainApp);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(StringResources.CREATE_NEW_PROJECT);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProjectAsAction() {
        DirectoryChooser saveProjectDialog = new DirectoryChooser();
        saveProjectDialog.setTitle(StringResources.SAVE_PROJECT_AS);
        File selectedDirectory = saveProjectDialog.showDialog(menuBar.getScene().getWindow());
        if (selectedDirectory != null) {
            saveProject(selectedDirectory);
        }
    }

    private void saveProject(File selectedDirectory) {
        File projectDir = new File(selectedDirectory, (String) AppParameters.getSetting(ClientConstants.PROJECT_NAME));
        projectDir.mkdir();
        AppParameters.setSetting(ClientConstants.PROJECT_DIR, projectDir);
        ManageProject manager = new ManageProject(mainApp);
        manager.saveProject(projectDir);
    }

    private void loadProjectAction() {
        DirectoryChooser saveProjectDialog = new DirectoryChooser();
        saveProjectDialog.setTitle(StringResources.OPEN_PROJECT);
        File selectedDirectory = saveProjectDialog.showDialog(menuBar.getScene().getWindow());
        if (selectedDirectory != null) {
            ManageProject manager = new ManageProject(mainApp);
            manager.loadProject(selectedDirectory);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainApp.processLoadedProject();
                }
            });
        }
    }

}
