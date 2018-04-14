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
package com.astrocytes.application.widgets;

import com.astrocytes.application.widgets.primitives.drawable.Paintable;

import java.util.*;

public class LayerManager {
    private Map<String, List<Paintable>> layers = new HashMap<String, List<Paintable>>();

    public String createLayer() {
        String key = UUID.randomUUID().toString();
        this.layers.put(key, new ArrayList<Paintable>());
        return key;
    }

    public void removeLayer(String key) {
        this.layers.remove(key);
    }

    public void clearAll() {
        this.layers.clear();
    }

    public List<Paintable> getLayer(String key) {
        return this.layers.get(key);
    }

    public List<Paintable> getAllPaintables() {
        List<Paintable> result = new ArrayList<Paintable>();

        for (List<Paintable> group : this.layers.values()) {
            result.addAll(group);
        }

        return result;
    }
}
