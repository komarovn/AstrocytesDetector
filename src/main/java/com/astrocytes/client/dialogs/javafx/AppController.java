package com.astrocytes.client.dialogs.javafx;

import com.astrocytes.client.App;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nikolay Komarov on 16.03.2017.
 */
public class AppController implements Initializable {

    private App mainApp;

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu file;

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
    private Pane graphWidgetPane;

    @FXML
    private SwingNode swingNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //mainPane.getTop().setPickOnBounds(false);
        //setGraphicalWidget();
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
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApp.executeExit();
            }
        });
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    private void setGraphicalWidget() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(mainApp.getGraphicalWidget());
                swingNode.setPickOnBounds(false);
                file.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        file.setDisable(true);
                    }
                });
            }
        });
    }
}
