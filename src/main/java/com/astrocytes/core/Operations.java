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
package com.astrocytes.core;

import com.astrocytes.core.primitives.Point;
import org.opencv.core.Mat;

import java.util.List;

public interface Operations {

    public void applyCannyEdgeDetection(Mat image, Integer minThreshold, Integer maxThreshold);

    public void setSourceImage(Mat sourceImage);

    public Mat getSourceImage();

    public Mat getOutputImage();

    /**
     * Making dilation and erosion with contours after Canny method applying.
     *
     * @param source - black and white image
     * @param radius - radius of structuring element; must be > 0
     */
    public Mat applyMathMorphology(Mat source, Integer radius);

    /**
     * Converting color image into gray image.
     *
     * @param source - source color image
     * @return grayscale image
     */
    public Mat convertGrayscale(Mat source);

    /**
     * @param source - source white-black image after Canny edge detection
     * @return color image with contours
     */
    public void drawAstrocyteCenters(Mat source);

    public Mat findAstrocytes(Mat source, Integer widthRectangle, Integer heightRectangle, Integer centerX, Integer centerY);

    public void drawLayerDelimiters(Mat source);

    public List<Point> getAstrocytesCenters();

}
