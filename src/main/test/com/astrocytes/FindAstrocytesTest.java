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
package com.astrocytes;

import com.astrocytes.application.App;
import com.astrocytes.application.connector.OperationsExecutor;
import com.astrocytes.core.data.DataProvider;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FindAstrocytesTest {
    private OperationsExecutor operationsExecutor = new OperationsExecutor();
    private BufferedImage testImage;

    private void prepare() {
        try {
            File file = new File("src/main/resources/presets/original-cortex.jpg");
            BufferedImage bufferedImage = ImageIO.read(file);
            operationsExecutor.setOriginalImage(bufferedImage);
            testImage = bufferedImage;
            DataProvider.setCannyMinThreshold(26);
            DataProvider.setCannyMaxThreshold(58);
            DataProvider.setRadiusMathMorphology(2);
            DataProvider.setScale(50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testCanny() {
        DataProvider.setCannyMinThreshold(26);
        DataProvider.setCannyMaxThreshold(58);
        DataProvider.setBoundingRectangleHeight(20);
        DataProvider.setBoundingRectangleWidth(20);
        DataProvider.setBoundingRectangleCenterX(10);
        DataProvider.setBoundingRectangleCenterY(10);
        testImage = operationsExecutor.applyCannyEdgeDetection();
    }

    private void run() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppTest appTest = new AppTest();
            }
        });
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
    }

    public static void main(String[] args) {
        FindAstrocytesTest test = new FindAstrocytesTest();
        test.prepare();
        //test.executeFindAstrocytes();
        //test.executeKmeans();
        //test.testCanny();
        //test.executeFindAstrocytes();
        test.run();
    }

    public class AppTest extends App {
        public AppTest() {
            super();
            image = testImage;
            updateCurrentView();
            menuController.setAvailability(true);
            getFrame().setSize(new Dimension(1000, 640));
        }
    }

}
