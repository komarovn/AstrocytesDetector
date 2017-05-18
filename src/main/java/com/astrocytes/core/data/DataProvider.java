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

import java.awt.image.BufferedImage;

public class DataProvider {

    public void setWorkingImage(BufferedImage image) {
        Data.setImage(image);
    }

    public BufferedImage getWorkingImage() {
        return Data.getImage();
    }

    public void setProjectDirectory(String path) {
        Data.setSetting(CoreConstants.PROJECT_DIR, path);
    }

    public String getProjectDirectory() {
        return (String) Data.getSetting(CoreConstants.PROJECT_DIR);
    }

    public void setProjectName(String name) {
        Data.setSetting(CoreConstants.PROJECT_NAME, name);
    }

    public String getProjectName() {
        return (String) Data.getSetting(CoreConstants.PROJECT_NAME);
    }

    public void setWindowWidth(Integer width) {
        Data.setSetting(CoreConstants.WINDOW_WIDTH, width);
    }

    public Integer getWindowWidth() {
        return (Integer) Data.getSetting(CoreConstants.WINDOW_WIDTH);
    }

    public void setWindowHeight(Integer height) {
        Data.setSetting(CoreConstants.WINDOW_HEIGHT, height);
    }

    public Integer getWindowHeight() {
        return (Integer) Data.getSetting(CoreConstants.WINDOW_HEIGHT);
    }

    public void setScale(Integer scale) {
        Data.setParameter(CoreConstants.SCALE, scale);
    }

    public Integer getScale() {
        return (Integer) Data.getParameter(CoreConstants.SCALE);
    }

    public void setCannyMinThreshold(Integer minThreshold) {
        Data.setParameter(CoreConstants.CANNY_MIN_THRESH, minThreshold);
    }

    public Integer getCannyMinThreshold() {
        return (Integer) Data.getParameter(CoreConstants.CANNY_MIN_THRESH);
    }

    public void setCannyMaxThreshold(Integer maxThreshold) {
        Data.setParameter(CoreConstants.CANNY_MIN_THRESH, maxThreshold);
    }

    public Integer getCannyMaxThreshold() {
        return (Integer) Data.getParameter(CoreConstants.CANNY_MAX_THRESH);
    }

    public void setRadiusMathMorphology(Integer radius) {
        Data.setParameter(CoreConstants.RADIUS_DIL_ER, radius);
    }

    public Integer getRadiusMathMorphology() {
        return (Integer) Data.getParameter(CoreConstants.RADIUS_DIL_ER);
    }

    public void setBoundingRectangleHeight(Integer height) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_HEIGHT, height);
    }

    public Integer getBoundingRectangleHeight() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_HEIGHT);
    }

    public void setBoundingRectangleWidth(Integer width) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_WIDTH, width);
    }

    public Integer getBoundingRectangleWidth() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_WIDTH);
    }

    public void setBoundingRectangleCenterX(Integer centerX) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_X, centerX);
    }

    public Integer getBoundingRectangleCenterX() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_X);
    }

    public void setBoundingRectangleCenterY(Integer centerY) {
        Data.setParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_Y, centerY);
    }

    public Integer getBoundingRectangleCenterY() {
        return (Integer) Data.getParameter(CoreConstants.BOUNDING_RECTANGLE_CENTER_Y);
    }

    public void destroyAllData() {
        Data.destroyImage();
        Data.destroyParameters();
        Data.destroySettings();
    }
}
