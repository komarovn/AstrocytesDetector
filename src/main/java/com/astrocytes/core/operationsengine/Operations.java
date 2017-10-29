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

import com.astrocytes.core.primitives.Point;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Instruments of Operations Engine
 */
public interface Operations {

    /**
     * Set an original working image to Operations Engine.
     * @param sourceImage - an original working image.
     */
    public void setSourceImage(Mat sourceImage);

    /**
     * Get an original working image in Operations Engine.
     * @return an original working image.
     */
    public Mat getSourceImage();

    /**
     * Get current working image in Operations Engine.
     * @return current working image.
     */
    public Mat getOutputImage();

    /**
     * Apply Canny edge detection algorythm for working image.
     *
     * @param image - a working image for applying Canny edge detection
     * @param minThreshold - minimal thresh
     * @param maxThreshold - maximal thresh
     */
    public void applyCannyEdgeDetection(Mat image, Integer minThreshold, Integer maxThreshold);

    /**
     * Make closing operation (dilation and erosion) for contours after applying Canny edge detection operation.
     *
     * @param source - binary image after applying Canny edge detection operation
     * @param radius - radius of structuring element; must be &gt; 0
     * @return a working image with applyed closing operation.
     */
    public Mat applyMathMorphology(Mat source, Integer radius);

    /**
     * Convert color image to grayscale image.
     *
     * @param source - source color image.
     * @return grayscale image of original one.
     */
    public Mat convertGrayscale(Mat source);

    /**
     * Fill an array of astrocytes' centers which are placed on original working image.
     * Steps of the algorythm:
     *   1) find and parameterize all contours on working image after Canny edge detection operation;
     *   2) check each contour area and delete all small contours;
     *   3) check size and form of the bounding rectangle of each contour;
     *   4) check circularity of the contour's form;
     *   5) check average intensity within each contour.
     *
     * @param source - working image after applying Canny edge detection and math morphology operations
     * @param widthRectangle - width of bounding rectangle
     * @param heightRectangle - height of bounding rectangle
     * @param centerX - x-coordinate of center of bounding rectangle by absolute value
     * @param centerY - y-coordinate of center of bounding rectangle by absolute value
     * @return original working image with colored astrocytes' centers.
     */
    public Mat findAstrocytes(Mat source, Integer widthRectangle, Integer heightRectangle, Integer centerX, Integer centerY);

    /**
     * Get a list of all astrocytes' centers finded by <code>findAstrocytes</code> operation.
     * @return a list of all astrocytes' centers or an empty list if <code>findAstrocytes</code> operation
     *     wasn't been applyed.
     */
    public List<Point> getAstrocytesCenters();

    public Mat applyKmeans(Mat source);

}