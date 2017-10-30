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
    private Mat sourceImage = new Mat();
    private Mat outputImage;

    private List<Point> astrocytesCenters;

    private List<Rect> boundingRectangles;

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
        if (minThreshold == null) {
            minThreshold = 0;
        }

        if (maxThreshold == null) {
            maxThreshold = 255;
        }

        Mat dest = convertGrayscale(image);
        Mat copy = new Mat(dest.rows(), dest.cols(), dest.type());

        double th2 = threshold(dest, new Mat(), 0, 255, THRESH_OTSU);

        GaussianBlur(dest, copy, new Size(9, 9), 1.4, 1.4);
        Canny(copy, dest, minThreshold, maxThreshold, 3, true);

        dest.copyTo(getOutputImage());
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
        prepareImage();
        //Mat result = CoreOperations.grayscale(source);
        //result.copyTo(getOutputImage());
        return getOutputImage();
    }

    @Override
    public Mat findAstrocytes(Mat source, Integer widthRectangle, Integer heightRectangle, Integer centerX, Integer centerY) {
        detectAstrocytes(source,
                (widthRectangle + heightRectangle) / 2,
                widthRectangle * heightRectangle * PI / 4,
                CoreOperations.intensity(sourceImage, centerX, centerY));
        drawAstrocyteCenters();
        drawBoundingRectangles();
        return getOutputImage();
    }

    private void detectAstrocytes(Mat source, Integer averageRectSize, Double averageArea, int intensity) {
        if (source.channels() == 3) {
            return;
        }

        astrocytesCenters = new ArrayList<>();
        boundingRectangles = new ArrayList<>();
        List<MatOfPoint> contoursAfterFirstIteration = new ArrayList<>();
        Mat hierarchy = new Mat();

        /* Step 1 */
        findContours(source, contoursAfterFirstIteration, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        for (MatOfPoint contour : contoursAfterFirstIteration) {
            Rect boundingRectangle = boundingRect(contour);
            Double contourArea = contourArea(contour);
            Double contourPerimeter = arcLength(new MatOfPoint2f(contour.toArray()), true);

            boundingRectangles.add(boundingRectangle);

            /* Step 2 */
            if (averageArea - 160 <= contourArea /*&& contourArea <= averageArea + 10*/) {
                /* Step 3 */
                if (((averageRectSize - 15 <= boundingRectangle.width) && (boundingRectangle.width <= averageRectSize + 15) ||
                        (averageRectSize - 15 <= boundingRectangle.height) && (boundingRectangle.height <= averageRectSize + 15)) &&
                        (boundingRectangle.width / (float) boundingRectangle.height < 1.8f) && (boundingRectangle.height / (float) boundingRectangle.width < 1.8f)) {
                    /* Step 4 */
                    if (contourArea / (contourPerimeter * contourPerimeter) > 0.05 && contourArea / (contourPerimeter * contourPerimeter) < 0.30) {
                        int averageIntensityWithinContour = CoreOperations.averageIntensity(sourceImage, contour);

                        /*int quantityOfPixelsWithinContour = 0;
                        for (int xCoord = (int) boundingRectangle.tl().x; xCoord <= (int) boundingRectangle.br().x; xCoord++) {
                            for (int yCoord = (int) boundingRectangle.tl().y; yCoord <= (int) boundingRectangle.br().y; yCoord++) {
                                if (pointPolygonTest(new MatOfPoint2f(contour.toArray()), new Point(xCoord, yCoord), false) > 0) {
                                    averageIntensityWithinContour += CoreOperations.intensity(sourceImage, xCoord, yCoord);
                                    quantityOfPixelsWithinContour++;
                                }
                            }
                        }
                        averageIntensityWithinContour /= quantityOfPixelsWithinContour;*/

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

    private void drawAstrocyteCenters() {
        if (astrocytesCenters == null) {
            return;
        }

        Mat dest = sourceImage.clone();

        for (Point center : astrocytesCenters) {
            Imgproc.circle(dest, center, 3, new Scalar(108, 240, 3));
        }

        dest.copyTo(getOutputImage());
    }

    private void drawBoundingRectangles() {
        Mat dest = sourceImage.clone();

        for (Rect rect : boundingRectangles) {
            Imgproc.rectangle(dest, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0.3, 0.5, 0.6), 1);
        }

        dest.copyTo(getOutputImage());
    }

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
        Mat dest = new Mat();//source.rows(), source.cols(), CvType.CV_32FC3);

        source.convertTo(source, CvType.CV_32F, 1.0 / 255.0);

        Mat centers = new Mat();
        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 20, 0.1);
        Core.kmeans(source, 4, labels, criteria, 10, Core.KMEANS_PP_CENTERS, centers);

        List<Mat> mats = showClusters(source, labels, centers);
        //mats.get(0).convertTo(dest, CvType.CV_8UC3);
        centers.convertTo(dest, CvType.CV_8UC3);
        dest.copyTo(getOutputImage());
        return dest;
    }

    private List<Mat> showClusters(Mat cutout, Mat labels, Mat centers) {
        centers.convertTo(centers, CvType.CV_8UC1, 255.0);
        centers.reshape(3);

        List<Mat> clusters = new ArrayList<Mat>();
        for(int i = 0; i < centers.rows(); i++) {
            clusters.add(Mat.zeros(cutout.size(), cutout.type()));
        }

        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for(int i = 0; i < centers.rows(); i++) {
            counts.put(i, 0);
        }

        for(int y = 0; y < cutout.rows(); y++) {
            int rows = 0;
            for(int x = 0; x < cutout.cols(); x++) {
                int label = (int)labels.get(rows, 0)[0];
                int r = (int)centers.get(label, 2)[0];
                int g = (int)centers.get(label, 1)[0];
                int b = (int)centers.get(label, 0)[0];
                counts.put(label, counts.get(label) + 1);
                clusters.get(label).put(y, x, b, g, r);
                rows++;
            }
        }
        System.out.println(counts);
        return clusters;
    }

    public void prepareImage() {
        Mat result = //CoreOperations.normalize(sourceImage);
                CoreOperations.grayscale(sourceImage);
        //result = CoreOperations.grayscale(result);
        result = CoreOperations.threshold(result, 197);
        result = CoreOperations.erode(result, 3);
        result = CoreOperations.invert(result);
        result = CoreOperations.clearContours(result, 125);

        Mat bigErode = CoreOperations.erode(result, 30);
        //result = CoreOperations.xor(result, bigErode);

        cvtColor(result, result, Imgproc.COLOR_GRAY2BGR);
        result = CoreOperations.and(sourceImage, result);

        //result = CoreOperations.threshold(result, 200, 80, 200);

        result.copyTo(getOutputImage());
    }
}
