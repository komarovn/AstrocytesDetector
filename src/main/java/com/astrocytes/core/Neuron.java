package com.astrocytes.core;

import org.opencv.core.Point;

public class Neuron {
    private Point center;
    private float radius;

    public Neuron(Point center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Neuron neuron = (Neuron) o;

        if (center == null && neuron.center != null || center != null && neuron.center == null) return false;
        if (center != null) {
            //TODO: compare coordinates
        }
        return center != null ? center.equals(neuron.center) : neuron.center == null;
    }

    @Override
    public int hashCode() {
        int result = center != null ? center.hashCode() : 0;
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        return result;
    }
}
