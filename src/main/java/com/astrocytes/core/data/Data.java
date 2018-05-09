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

import com.sun.istack.internal.NotNull;

import java.awt.image.BufferedImage;
import java.util.HashMap;

class Data {

    /**
     * settings   - storage of system setting (e.g. window size)
     * parameters - storage of project's parameters (e.g. values of thresholds)
     * image      - original working image
     */
    private HashMap<String, Object> settings;
    private HashMap<String, Object> parameters;
    private HashMap<String, Object> rawData;
    private BufferedImage image;
    private static Data singleton;

    private Data() {
        settings = new HashMap<>();
        parameters = new HashMap<>();
        rawData = new HashMap<>();
    }

    static {
        singleton = new Data();
    }

    static void setParameter(@NotNull String key, @NotNull Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.parameters.put(key, value);
    }

    static Object getParameter(@NotNull String key) {
        return singleton.parameters.get(key);
    }

    static void setSetting(@NotNull String key, @NotNull Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.settings.put(key, value);
    }

    static Object getSetting(@NotNull String key) {
        return singleton.settings.get(key);
    }

    static void setRawData(@NotNull String key, @NotNull Object value) {
        singleton.rawData.put(key, value);
    }

    static Object getRawData(@NotNull String key) {
        return singleton.rawData.get(key);
    }

    static BufferedImage getImage() {
        return singleton.image;
    }

    static void setImage(@NotNull BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        singleton.image = image;
    }

    static void destroyParameters() {
        singleton.parameters.clear();
    }

    static void destroySettings() {
        singleton.settings.clear();
    }

    static void destroyRawData() {
        singleton.rawData.clear();
    }

    static void destroyImage() {
        singleton.image = null;
    }

}
