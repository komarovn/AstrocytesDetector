package com.astrocytes.client;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nikolay Komarov on 19.03.2017.
 */
public abstract class SwingJavaFXHelper {

    public static Object initFX(final JFXPanel content, URL address) {
        try {
            FXMLLoader loader = new FXMLLoader(address);
            final Parent root = loader.load();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Scene scene = new Scene(root);
                    content.setScene(scene);
                }
            });
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
