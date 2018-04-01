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
import com.astrocytes.application.data.LocalStorage;
import com.astrocytes.application.resources.ApplicationConstants;
import com.astrocytes.application.widgets.instrument.DrawHorizontalLineInstrument;
import com.astrocytes.application.widgets.instrument.PointerInstrument;
import com.astrocytes.application.widgets.instrument.ZoomPanInstrument;
import com.astrocytes.application.widgets.message.WarningMessage;
import com.astrocytes.application.widgets.primitives.drawable.DrawingPolygonalChain;
import com.astrocytes.application.widgets.primitives.drawable.Paintable;
import com.astrocytes.core.ImageHelper;
import com.astrocytes.application.dialogs.DialogCannyEdgeDetection;
import com.astrocytes.application.dialogs.DialogDilateErode;
import com.astrocytes.application.dialogs.DialogFindAstrocytes;
import com.astrocytes.application.dialogs.NativeJFileChooser;
import com.astrocytes.application.dialogs.javafx.MenuController;
import com.astrocytes.application.dialogs.javafx.StatusBarController;
import com.astrocytes.application.dialogs.javafx.ToolbarController;
import com.astrocytes.application.resources.StringResources;
import com.astrocytes.application.widgets.ImageEditor;
import com.astrocytes.core.data.DataProvider;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

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
    private LocalStorage appData;

    /**
     * Create and build an application form.
     */
    public App() {
        mainPanel = new JPanel();
        frame.setIconImage(new ImageIcon(getClass().getResource("/img/icon/i16.png")).getImage());
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        initComponents();
        initSettings();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(ApplicationConstants.DEFAULT_WINDOW_WIDTH,
                ApplicationConstants.DEFAULT_WINDOW_HEIGHT));
        initListeners();
        initInstruments();
        this.appData = new LocalStorage();
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
        menuController.setAvailability(false);

        toolbarController = (ToolbarController) SwingJavaFXHelper.initFX(toolbar, getClass().getResource("/fxml/Toolbar.fxml"));
        toolbarController.setMainApp(this);

        statusBarController = (StatusBarController) SwingJavaFXHelper.initFX(statusBar, getClass().getResource("/fxml/StatusBar.fxml"));
        statusBarController.setMainApp(this);

        dataProvider.setWindowWidth(ApplicationConstants.DEFAULT_WINDOW_WIDTH);
        dataProvider.setWindowHeight(ApplicationConstants.DEFAULT_WINDOW_HEIGHT);
    }

    private void initListeners() {
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateGrahicalWidget();
                updateSizeSettings();
            }
        });
    }

    private void initInstruments() {
        this.graphicalWidget.addInstrument(new PointerInstrument());
        this.graphicalWidget.addInstrument(new ZoomPanInstrument());
        this.graphicalWidget.addInstrument(new DrawHorizontalLineInstrument());
    }

    private void initSettings() {
        dataProvider.setAstrocytesColor(ApplicationConstants.DEFAULT_ASTROCYTES_COLOR);
        dataProvider.setNeuronsColor(ApplicationConstants.DEFAULT_NEURONS_COLOR);
        dataProvider.setMajorLayersColor(ApplicationConstants.DEFAULT_MAJOR_LAYERS_COLOR);
        dataProvider.setMinorLayersColor(ApplicationConstants.DEFAULT_MINOR_LAYERS_COLOR);
    }

    private void updateGrahicalWidget() {
        int h = mainPanelBlock.getHeight();
        int w = mainPanelBlock.getWidth();
        graphicalWidget.setWidgetSize(w, h);
    }

    protected void updateCurrentView() {
        graphicalWidget.setImage(image);
        graphicalWidget.updateWidget();
    }

    private void updateWindowSize() {
        frame.setSize(new Dimension(dataProvider.getWindowWidth(), dataProvider.getWindowHeight()));
        frame.repaint();
    }

    private void updateSizeSettings() {
        dataProvider.setWindowWidth((int) frame.getSize().getWidth());
        dataProvider.setWindowHeight((int) frame.getSize().getHeight());
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

    public void resetAll() {
        this.image = null;
        dataProvider.destroyAllData();
        appData.clearAll();
        graphicalWidget.reset();
        initSettings();
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
            menuController.setAvailability(true);
        }
    }

    /**
     * Process creation of a new project
     * @param imagePath - path where loaded image is located
     */
    public void executeCreateNewProject(String projectName, File imagePath, Integer scale) {
        if (imagePath != null) {
            resetAll();

            dataProvider.setProjectName(projectName);
            dataProvider.setScale(scale);

            BufferedImage bufferedImage = ImageHelper.loadImage(imagePath);
            this.image = bufferedImage;
            operationsExecutor.setOriginalImage(bufferedImage);
            dataProvider.setWorkingImage(operationsExecutor.getOriginalImage());

            updateSizeSettings();
            updateWindowSize();
            updateCurrentView();
            updateGrahicalWidget();
            menuController.setAvailability(true);
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
                ImageHelper.saveImage(graphicalWidget.renderImage(), fileToSave);
            }
        }
        else {
            // TODO: show message about empty image
            WarningMessage warnPopup = new WarningMessage(frame, "There is no image to save");
        }
    }

    /**
     * Process operation of grayscale for the current image.
     */
    public void executeGrayscale() {
        if (image != null) {
            image = operationsExecutor.applyGrayscale();
            updateCurrentView();
        }
    }

    public void executeDrawLayers() {
        if (image != null) {
            List<DrawingPolygonalChain> layers = operationsExecutor.getLayers();

            for (int i = 0; i < layers.size(); i++) {
                layers.get(i).setColor(i == 0 || i == layers.size() - 1 ?
                        dataProvider.getMajorLayersColor() : dataProvider.getMinorLayersColor());
            }

            if (appData.getMainLayersKey() == null) {
                appData.setMainLayersKey(graphicalWidget.getObjectManager().createGroup());
            }
            if (appData.getLayersKey() == null) {
                appData.setLayersKey(graphicalWidget.getObjectManager().createGroup());
            }
            graphicalWidget.getObjectManager().getGroup(appData.getMainLayersKey()).clear();
            graphicalWidget.getObjectManager().getGroup(appData.getLayersKey()).clear();
            graphicalWidget.getObjectManager().getGroup(appData.getMainLayersKey()).add(layers.get(0));
            graphicalWidget.getObjectManager().getGroup(appData.getMainLayersKey()).add(layers.get(layers.size() - 1));
            graphicalWidget.getObjectManager().getGroup(appData.getLayersKey()).addAll(layers.subList(1, layers.size() - 2));

            if (appData.getNeuronsKey() != null) {
                executeFindNeurons();
            }

            if (appData.getAstrocytesKey() != null) {
                executeFindAstrocytesAuto();
            }

            updateCurrentView();
        }
    }

    public void executeFindNeurons() {
        if (image != null) {
            if (appData.getNeuronsKey() == null) {
                appData.setNeuronsKey(graphicalWidget.getObjectManager().createGroup());
            }
            graphicalWidget.getObjectManager().getGroup(appData.getNeuronsKey()).clear();
            graphicalWidget.getObjectManager().getGroup(appData.getNeuronsKey()).addAll(operationsExecutor.getNeurons());
            updateCurrentView();
        }
    }

    /**
     * Process Canny edge detection operation for the current work image.
     */
    public void executeEdgeDetection() {
        if (image != null) {
            DialogCannyEdgeDetection dialog = new DialogCannyEdgeDetection(this, graphicalWidget.getImage());
            if (dialog.isApplied()) {
                image = operationsExecutor.applyCannyEdgeDetection();
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
                image = operationsExecutor.applyDilateAndErode();
                updateCurrentView();
            }
        }
    }

    /**
     * Process find astrocytes operation for the current work image.
     */
    public void executeFindAstrocytes() {
        if (image != null) {
            DialogFindAstrocytes dialog = new DialogFindAstrocytes(this, graphicalWidget.getImage());
            if (dialog.isApplied()) {
                if (appData.getAstrocytesKey() == null) {
                    appData.setAstrocytesKey(graphicalWidget.getObjectManager().createGroup());
                }
                graphicalWidget.getObjectManager().getGroup(appData.getAstrocytesKey()).clear();
                graphicalWidget.getObjectManager().getGroup(appData.getAstrocytesKey()).addAll(operationsExecutor.applyFindAstocytes());
                updateCurrentView();
                menuController.setLayerStatisticsEnabled(true);
            }
        }
    }

    public void executeFindAstrocytesAuto() {
        if (image != null) {
            if (appData.getAstrocytesKey() == null) {
                appData.setAstrocytesKey(graphicalWidget.getObjectManager().createGroup());
            }
            graphicalWidget.getObjectManager().getGroup(appData.getAstrocytesKey()).clear();
            graphicalWidget.getObjectManager().getGroup(appData.getAstrocytesKey()).addAll(operationsExecutor.getAstrocytes());
            updateCurrentView();
        }
    }

    /*public void executeKmeans() {
        if (image != null) {
            image = operationsExecutor.applyKmeans(image);
            updateCurrentView();
        }
    }*/

    /**
     * Apply new Settings.
     */
    public void executeSettings() {
        if (appData.getNeuronsKey() != null) {
            for (Paintable obj : graphicalWidget.getObjectManager().getGroup(appData.getNeuronsKey())) {
                obj.setColor(dataProvider.getNeuronsColor());
            }
        }

        if (appData.getAstrocytesKey() != null) {
            for (Paintable obj : graphicalWidget.getObjectManager().getGroup(appData.getAstrocytesKey())) {
                obj.setColor(dataProvider.getAstrocytesColor());
            }
        }

        if (appData.getMainLayersKey() != null) {
            for (Paintable obj : graphicalWidget.getObjectManager().getGroup(appData.getMainLayersKey())) {
                obj.setColor(dataProvider.getMajorLayersColor());
            }
        }

        if (appData.getLayersKey() != null) {
            for (Paintable obj : graphicalWidget.getObjectManager().getGroup(appData.getLayersKey())) {
                obj.setColor(dataProvider.getMinorLayersColor());
            }
        }

        updateCurrentView();
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
        operationsExecutor = new OperationsExecutor();
        operationsExecutor.setOriginalImage(dataProvider.getWorkingImage());
        image = dataProvider.getWorkingImage();
        image = operationsExecutor.applyCannyEdgeDetection();
        //image = operationsExecutor.applyDilateAndErode(image);
        //image = operationsExecutor.applyFindAstocytes(image);
        menuController.setAvailability(true);
        graphicalWidget.reset();
        updateWindowSize();
        updateCurrentView();
        updateGrahicalWidget();
    }
}
