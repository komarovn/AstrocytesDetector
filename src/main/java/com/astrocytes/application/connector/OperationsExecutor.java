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

import com.astrocytes.application.widgets.primitives.drawable.DrawingCircle;
import com.astrocytes.application.widgets.primitives.drawable.DrawingLine;
import com.astrocytes.application.widgets.primitives.drawable.DrawingPolygonalChain;
import com.astrocytes.core.ImageHelper;
import com.astrocytes.core.operationsengine.OperationsImpl;
import com.astrocytes.core.operationsengine.Operations;
import com.astrocytes.core.data.DataProvider;
import com.astrocytes.core.primitives.Point;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public BufferedImage applyGrayscale() {
        Mat converted = operations.convertGrayscale();
        return ImageHelper.convertMatToBufferedImage(converted);
    }

    public BufferedImage applyCannyEdgeDetection() {
        Mat result = operations.applyCannyEdgeDetection(dataProvider.getCannyMinThreshold(),
                dataProvider.getCannyMaxThreshold());
        return ImageHelper.convertMatToBufferedImage(result);
    }

    public BufferedImage applyDilateAndErode() {
        Mat result = operations.applyMathMorphology(dataProvider.getRadiusMathMorphology());
        return ImageHelper.convertMatToBufferedImage(result);
    }

    public List<DrawingCircle> applyFindAstocytes() {
        List<DrawingCircle> result = new ArrayList<DrawingCircle>();
        List<Point> centers = operations.findAstrocytes(dataProvider.getBoundingRectangleWidth(),
                dataProvider.getBoundingRectangleHeight(),
                dataProvider.getBoundingRectangleCenterX(),
                dataProvider.getBoundingRectangleCenterY());

        for (Point astrocyte : centers) {
            result.add(new DrawingCircle(astrocyte.getX().doubleValue(), astrocyte.getY().doubleValue(), 6.0));
        }

        return result;
    }

    public List<DrawingCircle> getAstrocytes() {
        List<DrawingCircle> result = new ArrayList<DrawingCircle>();

        for (Point astrocyte : operations.getAstrocytesCenters()) {
            result.add(new DrawingCircle(astrocyte.getX().doubleValue(), astrocyte.getY().doubleValue(), 6.0));
        }

        return result;
    }

    public List<DrawingCircle> getNeurons() {
        List<DrawingCircle> result = new ArrayList<DrawingCircle>();

        for (Point neuron : operations.getNeuronsCenters()) {
            result.add(new DrawingCircle(neuron.getX().doubleValue(), neuron.getY().doubleValue(), 8.0, Color.orange));
        }

        return result;
    }

    public List<DrawingPolygonalChain> getLayers() {
        List<DrawingPolygonalChain> result = new ArrayList<DrawingPolygonalChain>();

        for (Map.Entry<Integer, List<Point>> delimiter : operations.getLayerDelimiters().entrySet()) {
            DrawingPolygonalChain chain = new DrawingPolygonalChain(Color.RED);

            for (int i = 0; i < delimiter.getValue().size() - 2; i++) {
                Point startPoint = delimiter.getValue().get(i);
                Point endPoint = delimiter.getValue().get(i + 1);
                chain.addChainPart(new DrawingLine((double) startPoint.getX(), (double) startPoint.getY(),
                        (double) endPoint.getX(), (double) endPoint.getY()));
            }

            result.add(chain);
        }

        return result;
    }

    public Operations getOperations() {
        return operations;
    }
}
