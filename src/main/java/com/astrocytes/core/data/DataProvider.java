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
package com.astrocytes.core.data;

import com.astrocytes.core.CoreConstants;
import com.astrocytes.core.primitives.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Gives an access to system's data storage via special methods.
 */
public class DataProvider {

    public static void setWorkingImage(BufferedImage image) {
        Data.setImage(image);
    }

    public static BufferedImage getWorkingImage() {
        return Data.getImage();
    }

    public static void setProjectDirectory(String path) {
        Data.setSetting(CoreConstants.PROJECT_DIR, path);
    }

    public static String getProjectDirectory() {
        return (String) Data.getSetting(CoreConstants.PROJECT_DIR);
    }

    public static void setProjectName(String name) {
        Data.setSetting(CoreConstants.PROJECT_NAME, name);
    }

    public static String getProjectName() {
        return (String) Data.getSetting(CoreConstants.PROJECT_NAME);
    }

    public static void setWindowWidth(Integer width) {
        Data.setSetting(CoreConstants.WINDOW_WIDTH, width);
    }

    public static Integer getWindowWidth() {
        return (Integer) Data.getSetting(CoreConstants.WINDOW_WIDTH);
    }

    public static void setWindowHeight(Integer height) {
        Data.setSetting(CoreConstants.WINDOW_HEIGHT, height);
    }

    public static Integer getWindowHeight() {
        return (Integer) Data.getSetting(CoreConstants.WINDOW_HEIGHT);
    }

    public static void setScale(Integer scale) {
        Data.setParameter(CoreConstants.SCALE, scale);
    }

    public static Integer getScale() {
        return (Integer) Data.getParameter(CoreConstants.SCALE);
    }

    public static void setAstrocytesColor(Color color) {
        Data.setParameter(CoreConstants.ASTROCYTES_COLOR, color);
    }

    public static Color getAstrocytesColor() {
        return (Color) Data.getParameter(CoreConstants.ASTROCYTES_COLOR);
    }

    public static void setNeuronsColor(Color color) {
        Data.setParameter(CoreConstants.NEURONS_COLOR, color);
    }

    public static Color getNeuronsColor() {
        return (Color) Data.getParameter(CoreConstants.NEURONS_COLOR);
    }

    public static void setMajorLayersColor(Color color) {
        Data.setParameter(CoreConstants.MAJOR_LAYERS_COLOR, color);
    }

    public static Color getMajorLayersColor() {
        return (Color) Data.getParameter(CoreConstants.MAJOR_LAYERS_COLOR);
    }

    public static void setMinorLayersColor(Color color) {
        Data.setParameter(CoreConstants.MINOR_LAYERS_COLOR, color);
    }

    public static Color getMinorLayersColor() {
        return (Color) Data.getParameter(CoreConstants.MINOR_LAYERS_COLOR);
    }

    public static void setCannyMinThreshold(Integer minThreshold) {
        Data.setParameter(CoreConstants.CANNY_MIN_THRESH, minThreshold);
    }

    public static Integer getCannyMinThreshold() {
        return (Integer) Data.getParameter(CoreConstants.CANNY_MIN_THRESH);
    }

    public static void setCannyMaxThreshold(Integer maxThreshold) {
        Data.setParameter(CoreConstants.CANNY_MAX_THRESH, maxThreshold);
    }

    public static Integer getCannyMaxThreshold() {
        return (Integer) Data.getParameter(CoreConstants.CANNY_MAX_THRESH);
    }

    public static void setCannyUseImage(Boolean value) {
        Data.setParameter(CoreConstants.IS_CANNY_USE_IMAGE, value);
    }

    public static Boolean isCannyUseImage() {
        return (Boolean) Data.getParameter(CoreConstants.IS_CANNY_USE_IMAGE);
    }

    public static void setRadiusMathMorphology(Integer radius) {
        Data.setParameter(CoreConstants.RADIUS_DIL_ER, radius);
    }

    public static Integer getRadiusMathMorphology() {
        return (Integer) Data.getParameter(CoreConstants.RADIUS_DIL_ER);
    }

    public static void setBoundingRectangleHeight(Integer height) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_HEIGHT, height);
    }

    public static Integer getBoundingRectangleHeight() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_HEIGHT);
    }

    public static void setBoundingRectangleWidth(Integer width) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_WIDTH, width);
    }

    public static Integer getBoundingRectangleWidth() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_WIDTH);
    }

    public static void setBoundingRectangleCenterX(Integer centerX) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_X, centerX);
    }

    public static Integer getBoundingRectangleCenterX() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_X);
    }

    public static void setBoundingRectangleCenterY(Integer centerY) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_Y, centerY);
    }

    public static Integer getBoundingRectangleCenterY() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_Y);
    }

    public static void setLayers(Map<Integer, List<Point>> layers) {
        Data.setRawData(CoreConstants.LAYERS, layers);
    }

    public static Map<Integer, List<Point>> getLayers() {
        return (Map<Integer, List<Point>>) Data.getRawData(CoreConstants.LAYERS);
    }

    public static void setNeurons(List<Point> neurons) {
        Data.setRawData(CoreConstants.NEURONS, neurons);
    }

    public static List<Point> getNeurons() {
        return (List<Point>) Data.getRawData(CoreConstants.NEURONS);
    }

    public static void setAstrocytes(List<Point> astrocytes) {
        Data.setRawData(CoreConstants.ASTROCYTES, astrocytes);
    }

    public static List<Point> getAstrocytes() {
        return (List<Point>) Data.getRawData(CoreConstants.ASTROCYTES);
    }

    /**
     * Clear all data stored in the system.
     */
    public static void destroyAllData() {
        Data.destroyImage();
        Data.destroyParameters();
        Data.destroySettings();
        Data.destroyRawData();
    }
}
