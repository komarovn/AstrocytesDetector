package com.astrocytes.client;

import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.dialogs.DialogCannyEdgeDetection;
import com.astrocytes.client.dialogs.DialogDilateErode;
import com.astrocytes.client.dialogs.DialogFindAstrocytes;
import com.astrocytes.client.dialogs.NativeJFileChooser;
import com.astrocytes.client.dialogs.javafx.AppController;
import com.astrocytes.client.dialogs.javafx.StatusBarController;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import com.astrocytes.server.OperationsImpl;
import com.astrocytes.shared.Operations;
import com.astrocytes.client.widgets.GraphicalWidget;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Nikolay Komarov on 23.02.2017.
 */
public class App {
    private JFrame frame = new JFrame(StringResources.ASTROCYTES_DETECTOR);
    private JPanel mainPanel;
    private GraphicalWidget graphicalWidget;
    private MainPanelBlock mainPanelBlock = new MainPanelBlock();

    /* JavaFX controllers */
    private AppController controller;
    private StatusBarController statusBarController;

    private Operations operations = new OperationsImpl();
    private BufferedImage image;

    public App() {
        mainPanel = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        initComponents();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(Integer.parseInt(ClientConstants.DEFAULT_WINDOW_WIDTH), Integer.parseInt(ClientConstants.DEFAULT_WINDOW_HEIGHT)));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage ownerStage = new Stage();
                controller.setStage(ownerStage);
                setResizeListener();
            }
        });
    }

    private void initComponents() {
        JFXPanel menuFromFxml = new JFXPanel();
        mainPanel.add(menuFromFxml, BorderLayout.PAGE_START);
        mainPanel.add(mainPanelBlock, BorderLayout.CENTER);
        JFXPanel statusBar = new JFXPanel();
        mainPanel.add(statusBar, BorderLayout.PAGE_END);

        controller = (AppController) SwingJavaFXHelper.initFX(menuFromFxml, getClass().getResource("/fxml/MenuBar.fxml"));
        controller.setMainApp(this);
        controller.setAvailability(true);

        statusBarController = (StatusBarController) SwingJavaFXHelper.initFX(statusBar, getClass().getResource("/fxml/StatusBar.fxml"));
        statusBarController.setMainApp(this);

        AppParameters.setSetting(ClientConstants.WINDOW_WIDTH, ClientConstants.DEFAULT_WINDOW_WIDTH);
        AppParameters.setSetting(ClientConstants.WINDOW_HEIGHT, ClientConstants.DEFAULT_WINDOW_HEIGHT);
    }

    private void setResizeListener() {
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateGrahicalWidget();
                AppParameters.setSetting(ClientConstants.WINDOW_WIDTH, String.valueOf((int) frame.getSize().getWidth()));
                AppParameters.setSetting(ClientConstants.WINDOW_HEIGHT, String.valueOf((int) frame.getSize().getHeight()));
            }
        });
    }

    private void updateGrahicalWidget() {
        int h = mainPanelBlock.getHeight();
        int w = mainPanelBlock.getWidth();
        graphicalWidget.updateWidget(w, h);
        graphicalWidget.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusBarController.setScaleValue((int) (graphicalWidget.getZoomScale() * 100));
                    }
                });
            }
        });
    }

    private void updateCurrentView() {
        graphicalWidget.setImage(image);
        graphicalWidget.updateWidget();
    }

    private void updateWindowSize() {
        frame.setSize(new Dimension(Integer.parseInt((String) AppParameters.getSetting(ClientConstants.WINDOW_WIDTH)),
                Integer.parseInt((String) AppParameters.getSetting(ClientConstants.WINDOW_HEIGHT))));
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

            graphicalWidget = new GraphicalWidget();
            add(graphicalWidget, gridBagConstraints);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public MainPanelBlock getMainPanelBlock() {
        return mainPanelBlock;
    }

    public GraphicalWidget getGraphicalWidget() {
        return graphicalWidget;
    }

    public Operations getOperations() {
        return operations;
    }

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
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Mat sourceImage = ImageHelper.convertBufferedImageToMat(bufferedImage);
                operations.setSourceImage(sourceImage);
                image = bufferedImage;
                updateWindowSize();
                updateCurrentView();
                updateGrahicalWidget();
                controller.setAvailability(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void executeExportImage() {
        if (image != null) {
            final NativeJFileChooser saveFileDialog = new NativeJFileChooser();
            saveFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("JPG Image", "jpg"));
            saveFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
            saveFileDialog.setAcceptAllFileFilterUsed(false);
            int result = saveFileDialog.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File fileToSave = saveFileDialog.getSelectedFile();
                BufferedImage bufferedImage = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                try {
                    ImageIO.write(bufferedImage, "png", fileToSave);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else {
            // TODO: show message about empty image
            WarningMessage warnPopup = new WarningMessage(frame, "There is no image to save");
        }
    }

    public void executeEdgeDetection() {
        if (image != null) {
            DialogCannyEdgeDetection dialog = new DialogCannyEdgeDetection(frame, graphicalWidget.getImage());
            if (dialog.getStatus()) {
                operations.applyCannyEdgeDetection(ImageHelper.convertBufferedImageToMat(image),
                        Integer.parseInt((String) AppParameters.getParameter(ClientConstants.CANNY_MIN_THRESH)),
                        Integer.parseInt((String) AppParameters.getParameter(ClientConstants.CANNY_MAX_THRESH)));
                image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                updateCurrentView();
            }
        }
    }

    public void executeDilateErode() {
        if (image != null) {
            DialogDilateErode dialog = new DialogDilateErode(frame, graphicalWidget.getImage());
            if (dialog.getStatus()) {
                operations.applyMathMorphology(ImageHelper.convertBufferedImageToMat(image),
                        Integer.parseInt((String) AppParameters.getParameter(ClientConstants.RADIUS_DIL_ER)));
                image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                updateCurrentView();
            }
        }
    }

    public void executeGrayscale() {
        if (image != null) {
            Mat converted = operations.convertGrayscale(ImageHelper.convertBufferedImageToMat(image));
            image = ImageHelper.convertMatToBufferedImage(converted);
            updateCurrentView();
        }
    }

    public void executeFindAstrocytes() {
        if (image != null) {
            DialogFindAstrocytes dialog = new DialogFindAstrocytes(frame);
            if (dialog.getStatus()) {
                operations.drawAstrocyteCenters(ImageHelper.convertBufferedImageToMat(image));
                image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                updateCurrentView();
            }
        }
    }

    public void executeExit() {
        frame.dispose();
    }

    public void processLoadedProject() {
        image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
        controller.setAvailability(false);
        updateWindowSize();
        updateCurrentView();
        updateGrahicalWidget();
    }
}
