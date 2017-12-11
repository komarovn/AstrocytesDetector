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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.PI;
import static org.opencv.imgproc.Imgproc.*;

public class OperationsImpl implements Operations {
    public static final float[] BRODMANN_COEFFS = {0.09f, 0.34f, 0.42f, 0.64f, 1.0f};

    private Mat sourceImage = new Mat();
    private Mat outputImage;
    private Mat preparedImage;

    private List<Point> astrocytesCenters;
    private List<Neuron> neurons;
    private Mat layerBounds;

    //private List<Rect> boundingRectangles;

    @Override
    public void setSourceImage(Mat sourceImage) {
        this.sourceImage = sourceImage;
    }

    @Override
    public Mat getSourceImage() {
        return sourceImage;
    }

    @Override
    public Mat getOutputImage() {
        if (outputImage == null) {
            outputImage = new Mat();
            sourceImage.copyTo(outputImage);
        }
        return outputImage;
    }

    @Override
    public void applyCannyEdgeDetection(Mat image, Integer minThreshold, Integer maxThreshold) {
        CoreOperations.cannyFilter(sourceImage, minThreshold, maxThreshold).copyTo(getOutputImage());
    }

    @Override
    public Mat applyMathMorphology(Mat source, Integer radius) {
        Mat dest = new Mat();
        int instrumentSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(instrumentSize, instrumentSize), new Point(radius, radius));

        Imgproc.morphologyEx(source, dest, MORPH_CLOSE, kernel, new Point(-1, -1), 1);

        dest.copyTo(getOutputImage());
        return dest;
    }

    @Override
    public Mat convertGrayscale(Mat source) {
        Mat result = CoreOperations.grayscale(source);
        result.copyTo(getOutputImage());
        return getOutputImage();
    }

    @Override
    public Mat findAstrocytes(Mat source, Integer widthRectangle, Integer heightRectangle, Integer centerX, Integer centerY) {
        detectAstrocytes(source,
                (widthRectangle + heightRectangle) / 2,
                widthRectangle * heightRectangle * PI / 4,
                CoreOperations.intensity(sourceImage, centerX, centerY));
        return drawAstrocyteCenters();
        //drawBoundingRectangles();
        //return getOutputImage();
    }

    @Deprecated
    private void detectAstrocytes(Mat source, Integer averageRectSize, Double averageArea, int intensity) {
        if (source.channels() == 3) {
            return;
        }

        astrocytesCenters = new ArrayList<>();
        //boundingRectangles = new ArrayList<>();
        List<MatOfPoint> contoursAfterFirstIteration = new ArrayList<>();
        Mat hierarchy = new Mat();

        /* Step 1 */
        findContours(source, contoursAfterFirstIteration, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        for (MatOfPoint contour : contoursAfterFirstIteration) {
            Rect boundingRectangle = boundingRect(contour);
            Double contourArea = contourArea(contour);
            Double contourPerimeter = arcLength(new MatOfPoint2f(contour.toArray()), true);

            //boundingRectangles.add(boundingRectangle);

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
        //dest.copyTo(getOutputImage());
    }

    private Mat drawNeuronsCenters() {
        if (neurons == null) {
            return sourceImage;
        }

        Mat result = sourceImage.clone();
        Scalar color = new Scalar(250, 10, 19);

        for (Neuron neuron : neurons) {
            Imgproc.circle(result, neuron.getCenter(), 4, color, 2);
        }

        return result;
    }

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

    /*private void drawBoundingRectangles() {
        Mat dest = sourceImage.clone();

        for (Rect rect : boundingRectangles) {
            Imgproc.rectangle(dest, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0.3, 0.5, 0.6), 1);
        }

        dest.copyTo(getOutputImage());
    }*/

    @Override
    public List<com.astrocytes.core.primitives.Point> getAstrocytesCenters() {
        if (astrocytesCenters == null) {
            return new ArrayList<com.astrocytes.core.primitives.Point>();
        }
        List<com.astrocytes.core.primitives.Point> result = new ArrayList<com.astrocytes.core.primitives.Point>();

        for (Point center : astrocytesCenters) {
            com.astrocytes.core.primitives.Point point = new com.astrocytes.core.primitives.Point((int) center.x, (int) center.y);
            result.add(point);
        }

        return result;
    }

    @Override
    public Mat applyKmeans(Mat source) {
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
        dest.copyTo(getOutputImage());
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

    @Override
    public void prepareImage() {
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

        // TODO: remove it later
        if (false) {
            findNeurons(result);
            result = drawNeuronsCenters();
        } else {
            //result = detectLayers();
            result = applyRayCastingSegmentation(result);
        }

        result.copyTo(getOutputImage());
        this.preparedImage = result;
    }

    public Mat detectLayers() {
        Mat equalizedImage = CoreOperations.invert(CoreOperations.equalize(sourceImage));

        int halfColumnWidth = 50;
        Mat density = new Mat(equalizedImage.rows(), equalizedImage.cols(), CvType.CV_32F);

        for (int i = 0; i < density.rows(); i++) {
            for (int j = 0; j < density.cols(); j++) {
                double p = 0.0;
                int leftBoundInterval = Math.max(j - halfColumnWidth, 0);
                int rightBoundInterval = Math.min(density.cols() - 1, j + halfColumnWidth);
                int intervalLength = rightBoundInterval - leftBoundInterval + 1;

                for (int s = leftBoundInterval; s <= rightBoundInterval; s++) {
                    p += equalizedImage.get(i, s)[0];
                }

                density.put(i, j, p / intervalLength);
            }
        }
        /*density.convertTo(density, CvType.CV_8UC1);
        return density;*/

        for (int j = 0; j < density.cols(); j++) {
            double intensity = 0.0;

            for (int i = 0; i < density.rows(); i++) {
                intensity += density.get(i, j)[0];
            }

            for (int i = 0; i < density.rows(); i++) {
                density.put(i, j, density.get(i, j)[0] / intensity);
            }
        }

        double ndlAverage = 1.0 / (double) density.rows();

        layerBounds = new Mat(6, density.cols(), CvType.CV_32F);
        double k1 = 0.56E-4;
        double k2 = 0.59E-4;

        astrocytesCenters = new ArrayList<Point>();

        float[] data = new float[density.rows() * (int) density.elemSize()];
        density.get(0, 10, data);

        Mat upperBoundExact = new Mat(1, density.cols(), CvType.CV_32F);
        Mat lowerBoundExact = new Mat(1, density.cols(), CvType.CV_32F);

        for (int j = 0; j < density.cols(); j++) {
            int upperBound = 0;
            int lowerBound = 0;

            for (int i = 0; i < density.rows(); i++) {
                if (density.get(i, j)[0] > ndlAverage + k1) {
                    upperBound = i;
                    break;
                }
            }
            for (int i = density.rows() - 1; i >= 0; i--) {
                if (density.get(i, j)[0] > ndlAverage + k2) {
                    lowerBound = i;
                    break;
                }
            }

            upperBoundExact.put(0, j, upperBound);
            lowerBoundExact.put(0, j, lowerBound);
        }

        //moving average for bounds
        int movingAverage = 200;
        for (int i = 0; i < upperBoundExact.cols(); i++) {
            int leftBoundInterval = Math.max(i - movingAverage, 0);
            int rightBoundInterval = Math.min(density.cols() - 1, i + movingAverage);
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

        return drawLayerBounds();
    }

    private void findNeurons(Mat preparedImage) {
        neurons = new ArrayList<Neuron>();
        int minNeuronRadius = 12;
        int maxNeuronRadius = 27;
        int stepSize = 3;

        for (int step = maxNeuronRadius; step >= minNeuronRadius; step -= stepSize) {
            Mat stepImage = CoreOperations.erode(preparedImage, step);

            for (Neuron neuron : neurons) {
                Scalar blackColor = new Scalar(0);
                Imgproc.circle(stepImage, neuron.getCenter(), (int) (1.8f * neuron.getRadius()), blackColor, Core.FILLED);
            }

            List<Neuron> neuronsInStep = findNeuronsInStep(stepImage, step);

            for (Neuron neuron : neuronsInStep) {
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
        //TODO: find contours, its centers (with bounding rectangle)
        return neurons;
    }

    @Override
    public Mat detectAstrocytes() {
        if (preparedImage == null) return sourceImage;

        Mat thresholdedImage = CoreOperations.threshold(preparedImage, 200, 80, 200);
        findAstrocytes(thresholdedImage);
        return drawAstrocyteCenters();
        //return getOutputImage();
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
        Mat cannyEdges = CoreOperations.cannyFilter(sourceImage, 26, 58);
        Mat contours = CoreOperations.drawAllContours(CoreOperations.erode(preparedImage, 5));
        Mat result = CoreOperations.or(CoreOperations.and(cannyEdges, CoreOperations.grayscale(preparedImage)), contours);
        cannyEdges.release();
        contours.release();

        return result;
    }
}
