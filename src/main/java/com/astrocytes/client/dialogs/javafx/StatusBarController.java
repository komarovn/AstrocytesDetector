package com.astrocytes.client.dialogs.javafx;

import com.astrocytes.client.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nikolay Komarov on 19.03.2017.
 */
public class StatusBarController implements Initializable {

    private App mainApp;

    @FXML
    private Pane statusBar;

    @FXML
    private Label scaleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setScaleValue(100);
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    public void setScaleValue(int scale) {
        scaleLabel.setText(String.valueOf(scale) + "%");
    }

}
