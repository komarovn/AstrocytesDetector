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
import com.astrocytes.core.data.DataProvider;
import com.astrocytes.core.exception.LoadProjectException;
import com.astrocytes.core.exception.SaveProjectException;
import com.astrocytes.core.manage.ProjectManager;
import com.astrocytes.application.resources.StringResources;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MenuController extends AbstractController {
    private ProjectManager manager = new ProjectManager();

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
    private MenuItem settings;

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
    private MenuItem findAstrocytesAutoMenuItem;

    @FXML
    private MenuItem findNeuronsMenuItem;

    @FXML
    private MenuItem drawLayersMenuItem;

    @FXML
    private MenuItem drawCellBoundsMenuItem;

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
        settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSettingsPanel();
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
        findAstrocytesAutoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeFindAstrocytesAuto();
            }
        });
        findNeuronsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeFindNeurons();
            }
        });
        drawLayersMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeDrawLayers();
            }
        });
        drawCellBoundsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeDrawCellBounds();
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

    public void setAvailability(boolean isAvailable) {
        saveProjectAs.setDisable(!isAvailable);
        exportImage.setDisable(!isAvailable);
        findNeuronsMenuItem.setDisable(!isAvailable);
        cannyEdDet.setDisable(!isAvailable);
        dilErode.setDisable(!isAvailable);
        grayscale.setDisable(!isAvailable);
        findAstrocytes.setDisable(!isAvailable);
        findAstrocytesAutoMenuItem.setDisable(!isAvailable);
        drawLayersMenuItem.setDisable(!isAvailable);
        drawCellBoundsMenuItem.setDisable(!isAvailable);
        layersStatistics.setDisable(true); //TODO: why constant value?
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
            stage.getIcons().add(new Image(getClass().getResource("/img/icon/i16.png").toString()));
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
        File projectDir = new File(selectedDirectory, DataProvider.getProjectName());
        projectDir.mkdir();
        DataProvider.setProjectDirectory(projectDir.getPath());
        try {
            manager.saveProject(projectDir);
        } catch (IOException|SaveProjectException e) {
            e.printStackTrace();
        }
    }

    private void loadProjectAction() {
        try {
            DirectoryChooser saveProjectDialog = new DirectoryChooser();
            saveProjectDialog.setTitle(StringResources.OPEN_PROJECT);
            File selectedDirectory = saveProjectDialog.showDialog(menuBar.getScene().getWindow());

            if (selectedDirectory != null) {
                manager.loadProject(selectedDirectory);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mainApp.processLoadedProject();
                    }
                });
            }
        } catch (LoadProjectException ex) {
            ex.printStackTrace();
        }
    }

    private void showSettingsPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml"));
            Parent root = fxmlLoader.load();
            SettingsController controller = fxmlLoader.getController();
            controller.setMainApp(mainApp);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(StringResources.SETTINGS);
            stage.getIcons().add(new Image(getClass().getResource("/img/icon/i16.png").toString()));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportLayerStatistics() {
        FileChooser exportDialog = new FileChooser();
        exportDialog.setTitle(StringResources.EXPORT_LAYER_STATISTICS);
        exportDialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Spreadsheet (*.xls)", "*.xls"));
        File file = exportDialog.showSaveDialog(menuBar.getScene().getWindow());
        if (file != null) {
            StatisticsExecutor.saveLayerStatisticsToXls(file);
        }
    }

}
