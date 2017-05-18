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
package com.astrocytes.application;

import com.astrocytes.application.connector.OperationsExecutor;
import com.astrocytes.application.resources.ApplicationConstants;
import com.astrocytes.application.widgets.message.WarningMessage;
import com.astrocytes.core.ImageHelper;
import com.astrocytes.core.data.Data;
import com.astrocytes.application.dialogs.DialogCannyEdgeDetection;
import com.astrocytes.application.dialogs.DialogDilateErode;
import com.astrocytes.application.dialogs.DialogFindAstrocytes;
import com.astrocytes.application.dialogs.NativeJFileChooser;
import com.astrocytes.application.dialogs.javafx.MenuController;
import com.astrocytes.application.dialogs.javafx.StatusBarController;
import com.astrocytes.application.dialogs.javafx.ToolbarController;
import com.astrocytes.core.CoreConstants;
import com.astrocytes.application.resources.StringResources;
import com.astrocytes.application.widgets.ImageEditor;
import com.astrocytes.core.data.DataProvider;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class App {
    private JFrame frame = new JFrame(StringResources.ASTROCYTES_DETECTOR);
    private JPanel mainPanel;
    private ImageEditor graphicalWidget;
    private MainPanelBlock mainPanelBlock = new MainPanelBlock();

    /* JavaFX controllers */
    protected MenuController menuController;
    private ToolbarController toolbarController;
    private StatusBarController statusBarController;

    private DataProvider dataProvider = new DataProvider();
    private OperationsExecutor operationsExecutor = new OperationsExecutor();
    protected BufferedImage image;

    /**
     * Create and build an application form.
     */
    public App() {
        mainPanel = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        initComponents();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(ApplicationConstants.DEFAULT_WINDOW_WIDTH,
                ApplicationConstants.DEFAULT_WINDOW_HEIGHT));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addResizeListener();
            }
        });
    }

    private void initComponents() {
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.PAGE_AXIS));
        JFXPanel menuFromFxml = new JFXPanel();
        upperPanel.add(menuFromFxml);
        JFXPanel toolbar = new JFXPanel();
        upperPanel.add(toolbar);
        mainPanel.add(upperPanel, BorderLayout.PAGE_START);

        mainPanel.add(mainPanelBlock, BorderLayout.CENTER);

        JFXPanel statusBar = new JFXPanel();
        mainPanel.add(statusBar, BorderLayout.PAGE_END);

        menuController = (MenuController) SwingJavaFXHelper.initFX(menuFromFxml, getClass().getResource("/fxml/MenuBar.fxml"));
        menuController.setMainApp(this);
        menuController.setAvailability(true);

        toolbarController = (ToolbarController) SwingJavaFXHelper.initFX(toolbar, getClass().getResource("/fxml/Toolbar.fxml"));
        toolbarController.setMainApp(this);

        statusBarController = (StatusBarController) SwingJavaFXHelper.initFX(statusBar, getClass().getResource("/fxml/StatusBar.fxml"));
        statusBarController.setMainApp(this);

        dataProvider.setWindowWidth(ApplicationConstants.DEFAULT_WINDOW_WIDTH);
        dataProvider.setWindowHeight(ApplicationConstants.DEFAULT_WINDOW_HEIGHT);
    }

    private void addResizeListener() {
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateGrahicalWidget();
                dataProvider.setWindowWidth((int) frame.getSize().getWidth());
                dataProvider.setWindowHeight((int) frame.getSize().getHeight());
            }
        });
    }

    private void updateGrahicalWidget() {
        int h = mainPanelBlock.getHeight();
        int w = mainPanelBlock.getWidth();
        graphicalWidget.updateWidget(w, h);
    }

    protected void updateCurrentView() {
        graphicalWidget.setImage(image);
        graphicalWidget.updateWidget();
    }

    private void updateWindowSize() {
        frame.setSize(new Dimension(dataProvider.getWindowWidth(), dataProvider.getWindowHeight()));
        frame.repaint();
    }

    protected class MainPanelBlock extends JPanel {

        public MainPanelBlock() {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 0;
            gridBagConstraints.weighty = 0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.fill = GridBagConstraints.BOTH;

            graphicalWidget = new ImageEditor();
            add(graphicalWidget, gridBagConstraints);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    /**
     * Get an image editor which is presented in application form.
     * @return an image editor.
     */
    public ImageEditor getGraphicalWidget() {
        return graphicalWidget;
    }

    public OperationsExecutor getOperationsExecutor() {
        return operationsExecutor;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    @Deprecated
    public void executeCreateNewProject() {
        final NativeJFileChooser openFileDialog = new NativeJFileChooser();
        openFileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("All Images", "jpg", "jpeg", "png", "bmp"));
        openFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Images", "jpg", "jpeg"));
        openFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        openFileDialog.setAcceptAllFileFilterUsed(false);
        int result = openFileDialog.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = openFileDialog.getSelectedFile();
            BufferedImage bufferedImage = ImageHelper.loadImage(file);
            operationsExecutor.setOriginalImage(bufferedImage);
            image = bufferedImage;
            updateWindowSize();
            updateCurrentView();
            updateGrahicalWidget();
            menuController.setAvailability(false);
        }
    }

    /**
     * Process creation of a new project
     * @param imagePath - path where loaded image is located
     */
    public void executeCreateNewProject(File imagePath) {
        if (imagePath != null) {
            BufferedImage bufferedImage = ImageHelper.loadImage(imagePath);
            this.image = bufferedImage;
            operationsExecutor.setOriginalImage(bufferedImage);
            dataProvider.setWorkingImage(operationsExecutor.getOriginalImage());
            updateWindowSize();
            updateCurrentView();
            updateGrahicalWidget();
            menuController.setAvailability(false);
        }
    }

    /**
     * Process export of current work image which is displayed in image editor.
     */
    public void executeExportImage() {
        if (image != null) {
            final NativeJFileChooser saveFileDialog = new NativeJFileChooser();
            saveFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("JPG Image", "jpg"));
            saveFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
            saveFileDialog.setAcceptAllFileFilterUsed(false);
            int result = saveFileDialog.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File fileToSave = saveFileDialog.getSelectedFile();
                BufferedImage bufferedImage = operationsExecutor.getCurrentImage();
                ImageHelper.saveImage(bufferedImage, fileToSave);
            }
        }
        else {
            // TODO: show message about empty image
            WarningMessage warnPopup = new WarningMessage(frame, "There is no image to save");
        }
    }

    /**
     * Process Canny edge detection operation for the current work image.
     */
    public void executeEdgeDetection() {
        if (image != null) {
            DialogCannyEdgeDetection dialog = new DialogCannyEdgeDetection(this, graphicalWidget.getImage());
            if (dialog.isApplied()) {
                image = operationsExecutor.applyCannyEdgeDetection(image);
                updateCurrentView();
            }
        }
    }

    /**
     * Process operations of math morphology: dilation and erosion for the current work image.
     */
    public void executeDilateErode() {
        if (image != null) {
            DialogDilateErode dialog = new DialogDilateErode(this, graphicalWidget.getImage());
            if (dialog.isApplied()) {
                image = operationsExecutor.applyDilateAndErode(image);
                updateCurrentView();
            }
        }
    }

    /**
     * Process operation of grayscale for the current image.
     */
    public void executeGrayscale() {
        if (image != null) {
            image = operationsExecutor.applyGrayscale(image);
            updateCurrentView();
        }
    }

    /**
     * Process find astrocytes operation for the current work image.
     */
    public void executeFindAstrocytes() {
        if (image != null) {
            DialogFindAstrocytes dialog = new DialogFindAstrocytes(this, graphicalWidget.getImage());
            if (dialog.isApplied()) {
                image = operationsExecutor.applyFindAstocytes(image);
                updateCurrentView();
                menuController.setLayerStatisticsEnabled(true);
            }
        }
    }

    /**
     * Process closing of the application form and exiting the program.
     */
    public void executeExit() {
        frame.dispose();
    }

    /**
     * Process chain of operations for loaded project.
     */
    public void processLoadedProject() {
        image = operationsExecutor.getCurrentImage();
        image = operationsExecutor.applyCannyEdgeDetection(image);
        image = operationsExecutor.applyDilateAndErode(image);
        //image = operationsExecutor.applyFindAstocytes(image);
        menuController.setAvailability(false);
        graphicalWidget.destroy();
        updateWindowSize();
        updateCurrentView();
        updateGrahicalWidget();
    }
}
