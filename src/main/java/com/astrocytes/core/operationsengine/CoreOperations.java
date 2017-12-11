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
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

/**
 * A bundle of basic operations for images, with customized some parameters
 * which are needed for project.
 */
public class CoreOperations {

    /**
     * Invert image.
     *
     * @param src - source image.
     * @return inverted image.
     */
    public static Mat invert(Mat src) {
        Mat dest = new Mat();
        Core.bitwise_not(src, dest);
        return dest;
    }

    /**
     * Applies thresholding for gray image.
     *
     * @param src - gray source image.
     * @param thresh - the level of threshold.
     * @return thresholded gray image.
     */
    public static Mat threshold(Mat src, int thresh) {
        Mat dest = new Mat();
        Imgproc.threshold(src, dest, thresh, 255, Imgproc.THRESH_BINARY);
        return dest;
    }

    /**
     * Applies thresholding for color image.
     *
     * @param src - color source image.
     * @param r - the value for red value in threshold.
     * @param g - the value for green value in threshold.
     * @param b - the value for blue value in threshold.
     * @return thresholded color image.
     */
    public static Mat threshold(Mat src, int r, int g, int b) {
        if (src.channels() < 3) return src;

        Mat dest = new Mat();
        Mat srcBin = new Mat();

        Imgproc.threshold(src, srcBin, 1, 255, Imgproc.THRESH_BINARY);
        Core.inRange(src, new Scalar(0), new Scalar(r, g, b), dest);
        dest = invert(dest);
        cvtColor(dest, dest, Imgproc.COLOR_GRAY2BGR);

        dest = xor(srcBin, dest);
        dest = and(src, dest);
        return dest;
    }

    /**
     * Converts a source color image to a gray image.
     *
     * @param src - BGR image.
     * @return gray image.
     */
    public static Mat grayscale(Mat src) {
        if (src.channels() < 3) return src;
        Mat dest = new Mat(src.rows(), src.cols(), CvType.CV_8UC1);
        cvtColor(src, dest, COLOR_BGR2GRAY);
        return dest;
    }

    /**
     * Applies morphological erosion.
     *
     * @param src - source image.
     * @param radius - radius of structure element.
     * @return eroded image.
     */
    public static Mat erode(Mat src, int radius) {
        Mat dest = new Mat();
        int kernelSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(kernelSize, kernelSize), new Point(radius, radius));
        Imgproc.erode(src, dest, kernel);
        return dest;
    }

    /**
     * Applies morphological dilation.
     *
     * @param src - source image.
     * @param radius - radius of structure element.
     * @return dilated image.
     */
    public static Mat dilate(Mat src, int radius) {
        Mat dest = new Mat();
        int kernelSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(kernelSize, kernelSize), new Point(radius, radius));
        Imgproc.dilate(src, dest, kernel);
        return dest;
    }

    public static Mat xor(Mat first, Mat second) {
        Mat dest = new Mat();
        Core.bitwise_xor(first, second, dest);
        return dest;
    }

    public static Mat and(Mat first, Mat second) {
        Mat dest = new Mat();
        Core.bitwise_and(first, second, dest);
        return dest;
    }

    public static Mat or(Mat first, Mat second) {
        Mat dest = new Mat();
        Core.bitwise_or(first, second, dest);
        return dest;
    }

    /**
     * Equalizes a histogram for the image (color).
     *
     * @param src - color image to be applyed auto contrast.
     * @return the source image with equalized histogram.
     */
    public static Mat equalize(Mat src) {
        Mat dest = new Mat();
        Imgproc.equalizeHist(grayscale(src), dest);
        return dest;
    }

    /**
     * Remove all small contours on binary image with areas less than specified threshold.
     *
     * @param src - binary source image.
     * @param thresh - minimum area of contour.
     * @return a source image with removed all contours with area less than {@param thresh}.
     */
    public static Mat clearContours(Mat src, int thresh) {
        if (src.channels() > 1) return src;

        Mat dest = src.clone();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        findContours(src, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        Mat maskWhite = new Mat(src.rows(), src.cols(), CvType.CV_8UC1, new Scalar(255));
        Mat maskBlack = maskWhite.clone();

        for (int i = 0; i < contours.size(); i++) {
            Double contourArea = contourArea(contours.get(i));

            if (contourArea < thresh) {
                int pixelColor = averageIntensity(src, contours.get(i));
                drawContours(pixelColor > 127 ? maskWhite : maskBlack, contours, i, new Scalar(0), Core.FILLED);
            }
        }

        maskWhite = erode(maskWhite, 2);
        maskBlack = erode(maskBlack, 2);
        dest = and(maskWhite, dest);
        dest = or(invert(maskBlack), dest);

        return dest;
    }

    /**
     * Calculates an average intensity of pixels on image within specified contour.
     *
     * @param src - source image used for calculating an average intensity.
     * @param contour - a contour which is presented as some region of interest for operation.
     * @return a level of average intensity.
     */
    public static int averageIntensity(Mat src, MatOfPoint contour) {
        int averageIntensityWithinContour = 0;
        int quantityOfPixelsWithinContour = 0;
        Rect boundingRectangle = boundingRect(contour);

        for (int xCoord = (int) boundingRectangle.tl().x; xCoord <= (int) boundingRectangle.br().x; xCoord++) {
            for (int yCoord = (int) boundingRectangle.tl().y; yCoord <= (int) boundingRectangle.br().y; yCoord++) {
                if (pointPolygonTest(new MatOfPoint2f(contour.toArray()), new Point(xCoord, yCoord), false) > 0) {
                    averageIntensityWithinContour += intensity(src, xCoord, yCoord);
                    quantityOfPixelsWithinContour++;
                }
            }
        }

        if (quantityOfPixelsWithinContour == 0) {
            quantityOfPixelsWithinContour = 1;
            averageIntensityWithinContour = intensity(src, boundingRectangle.x, boundingRectangle.y);
            if (src.channels() == 1) {
                averageIntensityWithinContour = averageIntensityWithinContour > 127 ? 0 : 255;
            }
        }

        return averageIntensityWithinContour / quantityOfPixelsWithinContour;
    }

    /**
     * Calculates an intensity of specified pixel on image. Use for color image.
     *
     * @param src - source image
     * @param x - a column of pixel on image.
     * @param y - a row of pixel on image.
     * @return an intensity of pixel ({@param x}, {@param y}).
     */
    public static int intensity(Mat src, int x, int y) {
        double[] pixel = src.get(y, x);

        if (pixel == null) return 0;

        if (pixel.length == 1) {
            return (int) pixel[0];
        }

        return (int) (0.11 * pixel[2] + 0.53 * pixel[1] + 0.36 * pixel[0]);
    }

    /**
     * Apply Gaussian blur on source image.
     *
     * @param src - source image.
     * @param size - kernel's size (must be odd).
     * @return image with applied Gaussian blur.
     */
    public static Mat gaussianBlur(Mat src, int size) {
        Mat dest = new Mat();
        GaussianBlur(src, dest, new Size(size, size), 1.4, 1.4);
        return dest;
    }

    /**
     * Apply Canny edge detector for image with given min and max thresholds.
     *
     * @param src - source image for applying filter.
     * @param minThresh - minimal threshold for filter.
     * @param maxThresh - maximum threshold for filter.
     * @return output image after applying Canny filter for {@param src} image.
     */
    public static Mat cannyFilter(Mat src, int minThresh, int maxThresh) {
        Mat result = new Mat();

        minThresh = Math.min(Math.max(minThresh, 0), 255);
        maxThresh = Math.max(Math.min(maxThresh, 255), 0);

        Mat blurredImage = gaussianBlur(grayscale(src), 9);
        Canny(blurredImage, result, minThresh, maxThresh, 3, true);

        return result;
    }

    /**
     * Draw all contours of source image.
     *
     * @param src - source image.
     * @return binary image with black background and white contours.
     */
    public static Mat drawAllContours(Mat src) {
        Mat result = new Mat(src.rows(), src.cols(), CvType.CV_8UC1);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        findContours(grayscale(src), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);
        for (int i = 0; i < contours.size(); i++) {
            drawContours(result, contours, i, new Scalar(255));
        }

        return result;
    }
}
