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
package com.astrocytes.core.operationsengine;

import com.astrocytes.core.Neuron;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.*;

import static java.lang.Math.PI;
import static org.opencv.imgproc.Imgproc.*;

public class OperationsImpl implements Operations {
    public static final float[] BRODMANN_COEFFS = {0.09f, 0.34f, 0.42f, 0.64f, 1.0f};

    /**
     * Original image. Can not be modified.
     */
    private Mat sourceImage = new Mat();
    /**
     * Current raster image. Can be modified due to applying raster operations only.
     */
    private Mat currentImage;
    /**
     * Internal image used in some calculations.
     */
    private Mat preparedImage;

    private List<Point> astrocytesCenters;
    private List<Neuron> neurons;
    private Mat layerBounds;

    @Override
    public void setSourceImage(Mat sourceImage) {
        this.sourceImage = sourceImage;
        this.currentImage = sourceImage.clone();
        this.astrocytesCenters = null;
        this.neurons = null;
        this.layerBounds = null;
    }

    @Override
    public Mat getSourceImage() {
        return sourceImage;
    }

    public void resetCurrentImage() {
        this.currentImage = sourceImage.clone();
    }

    @Override
    public Mat applyCannyEdgeDetection(Integer minThreshold, Integer maxThreshold) {
        CoreOperations.cannyFilter(currentImage, minThreshold, maxThreshold).copyTo(currentImage);
        return currentImage;
    }

    @Override
    public Mat applyMathMorphology(Integer radius) {
        Mat dest = new Mat();
        int instrumentSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(instrumentSize, instrumentSize), new Point(radius, radius));

        Imgproc.morphologyEx(currentImage, dest, MORPH_CLOSE, kernel, new Point(-1, -1), 1);

        dest.copyTo(currentImage);
        dest.release();
        return currentImage;
    }

    @Override
    public Mat convertGrayscale() {
        this.currentImage = CoreOperations.grayscale(this.currentImage);
        return this.currentImage;
    }

    @Override
    public List<com.astrocytes.core.primitives.Point> findAstrocytes(Integer widthRectangle, Integer heightRectangle, Integer centerX, Integer centerY) {
        detectAstrocytesOld(sourceImage.clone(),
                (widthRectangle + heightRectangle) / 2,
                widthRectangle * heightRectangle * PI / 4,
                CoreOperations.intensity(sourceImage, centerX, centerY));
        return getAstrocytesCenters();
    }

    private void detectAstrocytesOld(Mat source, Integer averageRectSize, Double averageArea, int intensity) {
        if (source.channels() == 3) {
            source = CoreOperations.grayscale(source);
        }

        astrocytesCenters = new ArrayList<>();
        List<MatOfPoint> contoursAfterFirstIteration = new ArrayList<>();
        Mat hierarchy = new Mat();

        /* Step 1 */
        findContours(source, contoursAfterFirstIteration, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        for (MatOfPoint contour : contoursAfterFirstIteration) {
            Rect boundingRectangle = boundingRect(contour);
            Double contourArea = contourArea(contour);
            Double contourPerimeter = arcLength(new MatOfPoint2f(contour.toArray()), true);

            /* Step 2 */
            if (averageArea - 160 <= contourArea /*&& contourArea <= averageArea + 10*/) {
                /* Step 3 */
                if (((averageRectSize - 15 <= boundingRectangle.width) && (boundingRectangle.width <= averageRectSize + 15) ||
                        (averageRectSize - 15 <= boundingRectangle.height) && (boundingRectangle.height <= averageRectSize + 15)) &&
                        (boundingRectangle.width / (float) boundingRectangle.height < 1.8f) && (boundingRectangle.height / (float) boundingRectangle.width < 1.8f)) {
                    /* Step 4 */
                    if (contourArea / (contourPerimeter * contourPerimeter) > 0.05 && contourArea / (contourPerimeter * contourPerimeter) < 0.30) {
                        int averageIntensityWithinContour = CoreOperations.averageIntensity(sourceImage, contour);

                        /* Step 5 */
                        if (averageIntensityWithinContour <= intensity + 20) {
                            int xCoordOfAstrocyteCenter = (int) boundingRectangle.tl().x + boundingRectangle.width / 2;
                            int yCoordOfAstrocyteCenter = (int) boundingRectangle.tl().y + boundingRectangle.height / 2;
                            astrocytesCenters.add(new Point(xCoordOfAstrocyteCenter, yCoordOfAstrocyteCenter));
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    private Mat drawAstrocyteCenters() {
        if (astrocytesCenters == null) {
            return sourceImage;
        }

        Mat result = sourceImage.clone();
        Scalar color = new Scalar(108, 240, 3);

        for (Point center : astrocytesCenters) {
            Imgproc.circle(result, center, 3, color);
        }

        return result;
    }

    @Deprecated
    private Mat drawNeuronsCenters(Mat src) {
        if (src == null) {
            src = sourceImage.clone();
        }
        if (neurons == null) {
            return src;
        }

        Mat result = src.clone();
        Scalar color = new Scalar(250, 10, 19);

        for (Neuron neuron : neurons) {
            Imgproc.circle(result, neuron.getCenter(), 4, color, 2);
        }

        return result;
    }

    @Deprecated
    private Mat drawLayerBounds() {
        if (layerBounds == null) {
            return sourceImage;
        }

        Mat result = sourceImage.clone();

        for (int i = 0; i < layerBounds.rows(); i++) {
            Scalar color = new Scalar(250, 20, 18);
            if (i == 0 || i == layerBounds.rows() - 1) {
                color = new Scalar(18, 20, 250);
            }

            for (int j = 0; j < layerBounds.cols(); j++) {
                Imgproc.circle(result, new Point(j, layerBounds.get(i, j)[0]), 1, color, -1);
            }
        }

        return result;
    }

    @Override
    public List<com.astrocytes.core.primitives.Point> getAstrocytesCenters() {
        if (astrocytesCenters == null) {
            detectAstrocytes();
        }
        List<com.astrocytes.core.primitives.Point> result = new ArrayList<com.astrocytes.core.primitives.Point>();

        for (Point center : astrocytesCenters) {
            int x = (int) center.x;
            int y = (int) center.y;

            if (this.layerBounds != null) {
                int upper = (int) this.layerBounds.get(0, x)[0];
                int lower = (int) this.layerBounds.get(this.layerBounds.rows() - 1, x)[0];

                if (y < upper || y > lower) {
                    continue;
                }
            }

            result.add(new com.astrocytes.core.primitives.Point(x, y));
        }

        return result;
    }

    @Override
    public List<com.astrocytes.core.primitives.Point> getNeuronsCenters() {
        if (neurons == null) {
            findNeurons();
        }
        List<com.astrocytes.core.primitives.Point> result = new ArrayList<com.astrocytes.core.primitives.Point>();

        for (Neuron neuron : neurons) {
            int x = (int) neuron.getCenter().x;
            int y = (int) neuron.getCenter().y;

            if (this.layerBounds != null) {
                int upper = (int) this.layerBounds.get(0, x)[0];
                int lower = (int) this.layerBounds.get(this.layerBounds.rows() - 1, x)[0];

                if (y < upper || y > lower) {
                    continue;
                }
            }

            result.add(new com.astrocytes.core.primitives.Point(x, y));
        }

        return result;
    }

    @Override
    public Map<Integer, List<com.astrocytes.core.primitives.Point>> getLayerDelimiters() {
        if (this.layerBounds == null) {
            detectLayers();
        }

        Map<Integer, List<com.astrocytes.core.primitives.Point>> result =
                new HashMap<Integer, List<com.astrocytes.core.primitives.Point>>();
        int stepSize = 10;

        for (int row = 0; row < this.layerBounds.rows(); row++) {
            List<com.astrocytes.core.primitives.Point> layer = new ArrayList<com.astrocytes.core.primitives.Point>();

            for (int col = 0; col < this.layerBounds.cols(); col = Math.min(this.layerBounds.cols() - 1, col + stepSize)) {
                int y = (int) this.layerBounds.get(row, col)[0];
                layer.add(new com.astrocytes.core.primitives.Point(col, y));
                if (col == this.layerBounds.cols() - 1) { break; }
            }

            result.put(row, layer);
        }

        return result;
    }

    // fills layerBounds with data
    private void detectLayers() {
        Mat equalizedImage = CoreOperations.invert(CoreOperations.equalize(sourceImage));

        int halfColumnWidth = 50;
        Mat density = new Mat(equalizedImage.rows(), equalizedImage.cols(), CvType.CV_32F);
        int rows = density.rows();
        int cols = density.cols();

        // > 1 min
        for (int i = 0; i < rows; i++) {
            double p;
            int leftBoundInterval, rightBoundInterval, intervalLength;
            for (int j = 0; j < cols; j++) {
                p = 0.0;
                leftBoundInterval = Math.max(j - halfColumnWidth, 0);
                rightBoundInterval = Math.min(cols - 1, j + halfColumnWidth);
                intervalLength = rightBoundInterval - leftBoundInterval + 1;

                for (int s = leftBoundInterval; s <= rightBoundInterval; s++) {
                    p += equalizedImage.get(i, s)[0];
                }

                density.put(i, j, p / intervalLength);
            }
        }

        //3 seconds
        for (int j = 0; j < cols; j++) {
            double intensity = 0.0;

            for (int i = 0; i < rows; i++) {
                intensity += density.get(i, j)[0];
            }

            for (int i = 0; i < rows; i++) {
                density.put(i, j, density.get(i, j)[0] / intensity);
            }
        }

        double ndlAverage = 1.0 / (double) rows;

        layerBounds = new Mat(6, cols, CvType.CV_32F);
        double k1 = 0.56E-4;
        double k2 = 1.3E-4;

        /*float[] data = new float[density.rows() * (int) density.elemSize()];
        density.get(0, 10, data);*/

        Mat upperBoundExact = new Mat(1, cols, CvType.CV_32F);
        Mat lowerBoundExact = new Mat(1, cols, CvType.CV_32F);

        for (int j = 0; j < cols; j++) {
            int upperBound = 0;
            int lowerBound = 0;

            for (int i = 0; i < rows; i++) {
                if (density.get(i, j)[0] > ndlAverage + k1) {
                    upperBound = i;
                    break;
                }
            }
            for (int i = rows - 1; i >= 0; i--) {
                if (density.get(i, j)[0] > ndlAverage + k2) {
                    lowerBound = i;
                    break;
                }
            }

            upperBoundExact.put(0, j, upperBound);
            lowerBoundExact.put(0, j, lowerBound);
        }

        //moving average for bounds
        int movingAverage = 300;
        for (int i = 0; i < upperBoundExact.cols(); i++) {
            int leftBoundInterval = Math.max(i - movingAverage, 0);
            int rightBoundInterval = Math.min(cols - 1, i + movingAverage);
            int intervalLength = rightBoundInterval - leftBoundInterval + 1;
            int upperBoundAverage = 0;
            int lowerBoundAverage = 0;

            for (int j = leftBoundInterval; j <= rightBoundInterval; j++) {
                upperBoundAverage += upperBoundExact.get(0, j)[0];
                lowerBoundAverage += lowerBoundExact.get(0, j)[0];
            }

            upperBoundAverage /= intervalLength;
            lowerBoundAverage /= intervalLength;
            int columnHeight = lowerBoundAverage - upperBoundAverage;
            layerBounds.put(0, i, upperBoundAverage);
            for (int h = 1; h < 5; h++) {
                layerBounds.put(h, i, upperBoundAverage + BRODMANN_COEFFS[h - 1] * columnHeight);
            }
            layerBounds.put(5, i, lowerBoundAverage);
        }
    }

    private void makePreparedImage() {
        Mat result = //CoreOperations.equalize(sourceImage);
                //CoreOperations.grayscale(sourceImage);
                CoreOperations.gaussianBlur(sourceImage, 5);
        result = CoreOperations.grayscale(result);
        result = CoreOperations.threshold(result, 195);
        result = CoreOperations.erode(result, 2);
        result = CoreOperations.invert(result);
        result = CoreOperations.clearContours(result, 190);

        Mat bigErode = CoreOperations.erode(result, 30);
        result = CoreOperations.xor(result, bigErode);

        cvtColor(result, result, Imgproc.COLOR_GRAY2BGR);
        result = CoreOperations.and(sourceImage, result);

        this.preparedImage = result;
    }

    private void findNeurons() {
        if (this.preparedImage == null) {
            makePreparedImage();
        }

        int minNeuronRadius = 12;
        int maxNeuronRadius = 27;
        int stepSize = 3;

        neurons = new ArrayList<Neuron>();

        for (int step = maxNeuronRadius; step >= minNeuronRadius; step -= stepSize) {
            Mat stepImage = CoreOperations.erode(this.preparedImage, step);

            for (Neuron neuron : neurons) {
                Scalar blackColor = new Scalar(0);
                Imgproc.circle(stepImage, neuron.getCenter(), (int) (1.8f * neuron.getRadius()), blackColor, Core.FILLED);
            }

            List<Neuron> neuronsInStep = findNeuronsInStep(stepImage, step);

            for (Neuron neuron : neuronsInStep) {
                //TODO: check for astrocyte collision
                if (!neurons.contains(neuron)) {
                    neurons.add(neuron);
                }
            }
        }
    }

    private List<Neuron> findNeuronsInStep(Mat source, int stepRadius) {
        List<Neuron> neurons = new ArrayList<Neuron>();

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        findContours(CoreOperations.grayscale(source), contours, hierarchy,
                Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        for (MatOfPoint contour : contours) {
            Rect boundingRectangle = boundingRect(contour);
            int xCenter = boundingRectangle.x + boundingRectangle.width / 2;
            int yCenter = boundingRectangle.y + boundingRectangle.height / 2;
            neurons.add(new Neuron(new Point(xCenter, yCenter), stepRadius));
        }

        return neurons;
    }

    private void detectAstrocytes() {
        if (preparedImage == null) {
            makePreparedImage();
        }

        Mat thresholdedImage = CoreOperations.threshold(preparedImage, 200, 80, 200);
        findAstrocytes(thresholdedImage);
    }

    private void findAstrocytes(Mat src) {
        astrocytesCenters = new ArrayList<Point>();

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        findContours(CoreOperations.grayscale(src), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        for (MatOfPoint contour : contours) {
            Rect boundingRectangle = boundingRect(contour);
            Double contourArea = contourArea(contour);
            Double contourPerimeter = arcLength(new MatOfPoint2f(contour.toArray()), true);

            if (contourArea / (contourPerimeter * contourPerimeter) > 0.05 &&
                    contourArea / (contourPerimeter * contourPerimeter) < 0.30) {
                int xCenter = boundingRectangle.x + boundingRectangle.width / 2;
                int yCenter = boundingRectangle.y + boundingRectangle.height / 2;
                astrocytesCenters.add(new Point(xCenter, yCenter));
            }
        }
    }

    private Mat applyRayCastingSegmentation(Mat preparedImage) {
        //Mat cannyEdges = CoreOperations.cannyFilter(sourceImage, 26, 58);
        Mat contours = new Mat(preparedImage.rows(), preparedImage.cols(), CvType.CV_32S);
        int contoursCount = /*neurons.size();*/ CoreOperations.drawAllContours(CoreOperations.erode(preparedImage, 5), contours);
        Mat result = new Mat(preparedImage.rows(), preparedImage.cols(), preparedImage.type());//CoreOperations.or(CoreOperations.and(cannyEdges, CoreOperations.grayscale(preparedImage)), contours);
        //cannyEdges.release();

        //Mat markers = new Mat(contours.rows(), contours.cols(), CvType.CV_32S);
        //contours.copyTo(markers);
        contours.convertTo(contours, CvType.CV_32S);

        for (Neuron neuron : neurons) {
            int x = (int) neuron.getCenter().x;
            int y = (int) neuron.getCenter().y;
            int color = (int) preparedImage.get(y, x)[0];
            /*contours.put(y, x, color);
            contours.put(y - 2, x, color);
            contours.put(y + 2, x, color);
            contours.put(y, x - 2, color);
            contours.put(y, x + 2, color);*/
            Imgproc.circle(contours, neuron.getCenter(), (int) (0.4f * neuron.getRadius()), new Scalar(color), -1);
        }

        Imgproc.watershed(sourceImage, contours);

        for (int i = 0; i < contours.rows(); i++ ) {
            for (int j = 0; j < contours.cols(); j++) {
                int index = (int) contours.get(i, j)[0];
                if (index == -1) {
                    result.put(i, j, 0, 0, 0);
                } else if (index <= 0 || index > contoursCount) {
                    result.put(i, j, 0, 0, 0);
                } else {
                    if (index == 255) {
                        result.put(i, j, 0, 0, 0/*sourceImage.get(i, j)*/);
                    } else {
                        result.put(i, j, index, index, index);
                    }
                }
            }
        }

        result = CoreOperations.erode(result, 2);
        result = CoreOperations.dilate(result, 3);

        contours.release();

        contours = sourceImage.clone();
        CoreOperations.drawAllContours(result, contours);

        return contours;
    }

    //---to be deleted

    private Mat applyKmeans(Mat source) {
        Mat dest = new Mat();

        source.convertTo(source, CvType.CV_32F, 1.0 / 255.0);

        Mat centers = new Mat();
        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 20, 0.1);
        Core.kmeans(source, 4, labels, criteria, 10, Core.KMEANS_PP_CENTERS, centers);

        List<Mat> mats = showClusters(source, labels, centers);
        //mats.get(0).convertTo(dest, CvType.CV_8UC3);
        Core.merge(mats, dest);
        //centers.convertTo(dest, CvType.CV_8UC3);
        return dest;
    }

    private List<Mat> showClusters(Mat cutout, Mat labels, Mat centers) {
        centers.convertTo(centers, CvType.CV_8UC1, 255.0);
        centers.reshape(3);

        List<Mat> clusters = new ArrayList<Mat>();
        for (int i = 0; i < centers.rows(); i++) {
            clusters.add(Mat.zeros(cutout.size(), cutout.type()));
        }

        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for (int i = 0; i < centers.rows(); i++) {
            counts.put(i, 0);
        }

        for (int y = 0; y < cutout.rows(); y++) {
            int rows = 0;
            for (int x = 0; x < cutout.cols(); x++) {
                int label = (int) labels.get(rows, 0)[0];
                int r = (int) centers.get(label, 2)[0];
                int g = (int) centers.get(label, 1)[0];
                int b = (int) centers.get(label, 0)[0];
                counts.put(label, counts.get(label) + 1);
                clusters.get(label).put(y, x, b, g, r);
                rows++;
            }
        }
        System.out.println(counts);
        return clusters;
    }
}
