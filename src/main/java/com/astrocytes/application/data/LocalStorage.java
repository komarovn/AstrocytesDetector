/*
 * Copyright (c) Lobachevsky University, 2018. All rights reserved.
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
package com.astrocytes.application.data;

public class LocalStorage {
    private String astrocytesKey;
    private String neuronsKey;
    private String mainLayersKey;
    private String layersKey;

    public String getAstrocytesKey() {
        return astrocytesKey;
    }

    public void setAstrocytesKey(String astrocytesKey) {
        this.astrocytesKey = astrocytesKey;
    }

    public String getNeuronsKey() {
        return neuronsKey;
    }

    public void setNeuronsKey(String neuronsKey) {
        this.neuronsKey = neuronsKey;
    }

    public String getMainLayersKey() {
        return mainLayersKey;
    }

    public void setMainLayersKey(String mainLayersKey) {
        this.mainLayersKey = mainLayersKey;
    }

    public String getLayersKey() {
        return layersKey;
    }

    public void setLayersKey(String layersKey) {
        this.layersKey = layersKey;
    }

    public void clearAll() {
        this.astrocytesKey = null;
        this.neuronsKey = null;
        this.mainLayersKey = null;
        this.layersKey = null;
    }
}
