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

import com.astrocytes.application.widgets.instrument.InstrumentType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ToolbarController extends AbstractController {
    private List<ToggleButton> toolbarButtons = new ArrayList<>();

    @FXML
    private ToggleButton cursorButton;

    @FXML
    private ToggleButton zoomAndPanButton;

    @FXML
    private ToggleButton horizontalLineButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toolbarButtons.add(cursorButton);
        toolbarButtons.add(zoomAndPanButton);
        toolbarButtons.add(horizontalLineButton);
        setImageOnButton(cursorButton, "pointer");
        setImageOnButton(zoomAndPanButton, "zoom-and-pan");
        setImageOnButton(horizontalLineButton, "h-line");
        addListeners();
    }

    private void addListeners() {
        cursorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectButton(cursorButton);
                mainApp.getGraphicalWidget().selectInstrument(InstrumentType.POINTER);
            }
        });
        zoomAndPanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectButton(zoomAndPanButton);
                mainApp.getGraphicalWidget().selectInstrument(InstrumentType.ZOOM_AND_PAN);
            }
        });
        horizontalLineButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectButton(horizontalLineButton);
                mainApp.getGraphicalWidget().selectInstrument(InstrumentType.LINE_HORIZONTAL);
            }
        });
    }

    private void selectButton(ToggleButton selectedButton) {
        for (ToggleButton button : toolbarButtons) {
            button.setSelected(false);
        }
        selectedButton.setSelected(true);
    }

    private void setImageOnButton(ToggleButton button, String imgName) {
        Image image = new Image(getClass().getResourceAsStream("/img/"+ imgName + ".png"));
        button.setGraphic(new ImageView(image));
        button.setPadding(Insets.EMPTY);
    }

}
