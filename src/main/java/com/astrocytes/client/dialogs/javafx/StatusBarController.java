package com.astrocytes.client.dialogs.javafx;

import com.astrocytes.client.App;
import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.resources.ClientConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
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

    @FXML
    private Pane scaleBar;

    @FXML
    private Label scaleBarLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setScaleValue(100);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainApp.getGraphicalWidget().addMouseWheelListener(new MouseAdapter() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setScaleValue((int) (mainApp.getGraphicalWidget().getZoomScale() * 100));
                                setScaleBarValue();
                            }
                        });
                    }
                });
            }
        });
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    private void setScaleValue(int scale) {
        scaleLabel.setText(String.valueOf(scale) + "%");
    }

    private void setScaleBarValue() {
        int scaleCoeff = Integer.parseInt(((String) AppParameters.getParameter(ClientConstants.SCALE))) * 2;
        scaleBar.setPrefWidth((int) (mainApp.getGraphicalWidget().getZoomScale() * scaleCoeff));
        scaleBarLabel.setText(mainApp.getGraphicalWidget().getZoomScale() * scaleCoeff + " μm");
    }

}
