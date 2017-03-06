package com.astrocytes.client;

import com.astrocytes.client.dialogs.DialogCannyEdgeDetection;
import com.astrocytes.client.dialogs.DialogDilateErode;
import com.astrocytes.client.dialogs.DialogFindAstrocytes;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.client.resources.StringResources;
import com.astrocytes.server.OperationsImpl;
import com.astrocytes.shared.AppParameters;
import com.astrocytes.shared.Operations;
import com.astrocytes.client.widgets.GraphicalWidget;
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

    private Operations operations = new OperationsImpl();
    private BufferedImage image;

    public App() {
        mainPanel = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel);
        setMenuBar(frame);
        setMainPanel();
        setResizeListener();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(1000, 600));
        frame.repaint();
    }

    private void setMainPanel() {
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        mainPanel.add(mainPanelBlock, gridBagConstraints);
    }

    private void setResizeListener() {
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateGrahicalWidget();
            }
        });
    }

    private void setMenuBar(final JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu(StringResources.FILE);
        JMenu operationsMenu = new JMenu(StringResources.OPERATIONS);

        JMenuItem openFile = new JMenuItem(StringResources.OPEN_FILE);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser openFileDialog = new JFileChooser();
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
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        JMenuItem saveAs = new JMenuItem(StringResources.SAVE_AS);
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    final JFileChooser saveFileDialog = new JFileChooser();
                    saveFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("JPG Image", "jpg"));
                    saveFileDialog.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
                    saveFileDialog.setAcceptAllFileFilterUsed(false);
                    int result = saveFileDialog.showSaveDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = saveFileDialog.getSelectedFile();
                        BufferedImage bufferedImage = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                        try {
                            String type = saveFileDialog.getTypeDescription(fileToSave);
                            ImageIO.write(bufferedImage, "png", fileToSave);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else {
                    // TODO: show message about empty image
                }
            }
        });
        JMenuItem exit = new JMenuItem(StringResources.EXIT);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JMenuItem edgeDetection = new JMenuItem(StringResources.EDGE_DETECTION);
        edgeDetection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.SHIFT_MASK));
        edgeDetection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    DialogCannyEdgeDetection dialog = new DialogCannyEdgeDetection(frame);
                    if (dialog.getStatus()) {
                        operations.applyCannyEdgeDetection(ImageHelper.convertBufferedImageToMat(image),
                                (Integer) AppParameters.getParameter(ClientConstants.CANNY_MIN_THRESH),
                                (Integer) AppParameters.getParameter(ClientConstants.CANNY_MAX_THRESH));
                        image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                        updateCurrentView();
                    }
                }
            }
        });
        JMenuItem dilateAndErode = new JMenuItem(StringResources.DILATE_AND_ERODE);
        dilateAndErode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    DialogDilateErode dialog = new DialogDilateErode(frame);
                    if (dialog.getStatus()) {
                        operations.applyMathMorphology(ImageHelper.convertBufferedImageToMat(image),
                                (Integer) AppParameters.getParameter(ClientConstants.RADIUS_DIL_ER));
                        image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                        updateCurrentView();
                    }
                }
            }
        });
        JMenuItem grayscale = new JMenuItem(StringResources.GRAYSCALE);
        grayscale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    Mat converted = operations.convertGrayscale(ImageHelper.convertBufferedImageToMat(image));
                    image = ImageHelper.convertMatToBufferedImage(converted);
                    updateCurrentView();
                }
            }
        });
        JMenuItem findAstrocytes = new JMenuItem(StringResources.FIND_ASTROCYTES);
        findAstrocytes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    DialogFindAstrocytes dialog = new DialogFindAstrocytes(frame);
                    if (dialog.getStatus()) {
                        operations.drawAstrocyteCenters(ImageHelper.convertBufferedImageToMat(image));
                        image = ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
                        updateCurrentView();
                    }
                }
            }
        });

        file.add(openFile);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);
        operationsMenu.add(edgeDetection);
        operationsMenu.add(dilateAndErode);
        operationsMenu.add(grayscale);
        operationsMenu.add(findAstrocytes);
        menuBar.add(file);
        menuBar.add(operationsMenu);

        frame.setJMenuBar(menuBar);
    }

    private void updateGrahicalWidget() {
        int h = mainPanelBlock.getHeight();
        int w = mainPanelBlock.getWidth();
        graphicalWidget.updateWidget(w, h);
    }

    private void updateCurrentView() {
        graphicalWidget.setImage(image);
        graphicalWidget.updateWidget();
    }

    private void updateWindowSize() {
        frame.setSize(new Dimension(1920, 1080));
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

}
