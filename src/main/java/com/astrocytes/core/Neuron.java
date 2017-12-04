package com.astrocytes.core;

import org.opencv.core.Point;

public class Neuron {
    private Point center;
    private float radius;
    private float epsilon = 5.0f;

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

        if (center == null && neuron.center != null ||
                center != null && neuron.center == null) return false;

        if (center != null) {
            //TODO: compare coordinates
            if (center.x - epsilon <= neuron.center.x && neuron.center.x <= center.x + epsilon &&
                    center.y - epsilon <= neuron.center.y && neuron.center.y <= center.y + epsilon) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = center != null ? center.hashCode() : 0;
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        return result;
    }
}
