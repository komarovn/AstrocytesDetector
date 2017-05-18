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

import com.astrocytes.application.connector.StatisticsExecutor;
import com.astrocytes.core.data.AppParameters;
import com.astrocytes.core.data.ManageProject;
import com.astrocytes.core.CoreConstants;
import com.astrocytes.application.resources.StringResources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MenuController extends AbstractController {

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

    @FXML
    private MenuItem layersStatistics;

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

        /* ---Statistics --- */
        layersStatistics.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exportLayerStatistics();
            }
        });
    }

    public void setAvailability(boolean isEmpty) {
        saveProjectAs.setDisable(isEmpty);
        exportImage.setDisable(isEmpty);
        cannyEdDet.setDisable(isEmpty);
        dilErode.setDisable(isEmpty);
        grayscale.setDisable(isEmpty);
        findAstrocytes.setDisable(isEmpty);
        layersStatistics.setDisable(true);
    }

    public void setLayerStatisticsEnabled(boolean isEnabled) {
        layersStatistics.setDisable(!isEnabled);
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
        File projectDir = new File(selectedDirectory, (String) AppParameters.getSetting(CoreConstants.PROJECT_NAME));
        projectDir.mkdir();
        AppParameters.setSetting(CoreConstants.PROJECT_DIR, projectDir);
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

    private void exportLayerStatistics() {
        FileChooser exportDialog = new FileChooser();
        exportDialog.setTitle(StringResources.EXPORT_LAYER_STATISTICS);
        exportDialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Spreadsheet (*.xls)", "*.xls"));
        File file = exportDialog.showSaveDialog(menuBar.getScene().getWindow());
        if (file != null) {
            StatisticsExecutor.saveLayerStatisticsToXls(mainApp.getOperationsExecutor().getOperations().getAstrocytesCenters(),
                    mainApp.getGraphicalWidget().getHorizontalLines(), file);
        }
    }

}
