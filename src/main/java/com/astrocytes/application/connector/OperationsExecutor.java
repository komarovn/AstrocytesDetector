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
package com.astrocytes.application.connector;

import com.astrocytes.core.ImageHelper;
import com.astrocytes.core.operationsengine.OperationsImpl;
import com.astrocytes.core.operationsengine.Operations;
import com.astrocytes.core.data.DataProvider;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public class OperationsExecutor {
    private Operations operations = new OperationsImpl();
    private DataProvider dataProvider = new DataProvider();

    public void setOriginalImage(BufferedImage in) {
        Mat sourceImage = ImageHelper.convertBufferedImageToMat(in);
        operations.setSourceImage(sourceImage);
    }

    public BufferedImage getOriginalImage() {
        return ImageHelper.convertMatToBufferedImage(operations.getSourceImage());
    }

    public BufferedImage applyCannyEdgeDetection(BufferedImage in) {
        operations.applyCannyEdgeDetection(ImageHelper.convertBufferedImageToMat(in),
                dataProvider.getCannyMinThreshold(),
                dataProvider.getCannyMaxThreshold());
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public BufferedImage applyDilateAndErode(BufferedImage in) {
        operations.applyMathMorphology(ImageHelper.convertBufferedImageToMat(in),
                dataProvider.getRadiusMathMorphology());
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public BufferedImage applyGrayscale(BufferedImage in) {
        Mat converted = operations.convertGrayscale(ImageHelper.convertBufferedImageToMat(in));
        return ImageHelper.convertMatToBufferedImage(converted);
    }

    public BufferedImage applyFindAstocytes(BufferedImage in) {
        operations.findAstrocytes(ImageHelper.convertBufferedImageToMat(in),
                dataProvider.getBoundingRectangleWidth(),
                dataProvider.getBoundingRectangleHeight(),
                dataProvider.getBoundingRectangleCenterX(),
                dataProvider.getBoundingRectangleCenterY());
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public BufferedImage getCurrentImage() {
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public Operations getOperations() {
        return operations;
    }
}
