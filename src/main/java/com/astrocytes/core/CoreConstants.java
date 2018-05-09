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

public abstract class CoreConstants {

    /* Settings */
    public static final String WINDOW_WIDTH = "windowWidth";
    public static final String WINDOW_HEIGHT = "windowHeight";

    public static final String PROJECT_DIR = "projectDir";
    public static final String PROJECT_NAME = "projectName";

    /* Parameters */
    public static final String SCALE = "scale";
    public static final String CANNY_MIN_THRESH = "cannyMinThresh";
    public static final String CANNY_MAX_THRESH = "cannyMaxThresh";
    public static final String RADIUS_DIL_ER = "radiusDilEr";

    public static final String BOUNDING_RECTANGLE_HEIGHT = "boundingRectangleHeight";
    public static final String BOUNDING_RECTANGLE_WIDTH = "boundingRectangleWidth";
    public static final String BOUNDING_RECTANGLE_CENTER_X = "boundingRectangleCenterX";
    public static final String BOUNDING_RECTANGLE_CENTER_Y = "boundingRectangleCenterY";

    public static final String ASTROCYTES_COLOR = "astrocytesColor";
    public static final String NEURONS_COLOR = "neuronsColor";
    public static final String MAJOR_LAYERS_COLOR = "majorLayersColor";
    public static final String MINOR_LAYERS_COLOR = "minorLayersColor";

    /* RAW data */
    public static final String LAYERS = "layers";
    public static final String NEURONS = "neurons";
    public static final String ASTROCYTES = "astrocytes";

    /* File names */
    public static final String FILE_SETTINGS = "settings.xml";
    public static final String FILE_PARAMETERS = "parameters.xml";
    public static final String FILE_IMAGE = "image.jpg";

    /* Statistics */
    public static final String XLS_SPREADSHEET_TITLE = "Statistics by Layers";
    public static final String XLS_LAYERS_TITLE = "";
    public static final String XLS_ASTROCYTES_TITLE = "Astrocytes Count";
    public static final String XLS_NEURONS_TITLE = "Neurons Count";

}
