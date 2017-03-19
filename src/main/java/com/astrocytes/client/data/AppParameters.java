package com.astrocytes.client.data;

import java.util.HashMap;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class AppParameters {

    /**
     * settings   - storage of system setting (e.g. window size)
     * parameters - storage of project's parameters (e.g. values of thresholds)
     */
    private HashMap settings;
    private HashMap parameters;
    private static AppParameters singleton;

    private AppParameters() {
        settings = new HashMap();
        parameters = new HashMap();
    }

    static {
        singleton = new AppParameters();
    }

    public static void setParameter(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.parameters.put(key, value);
    }

    public static Object getParameter(String key) {
        return singleton.parameters.get(key);
    }

    public static void setSetting(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.settings.put(key, value);
    }

    public static Object getSetting(String key) {
        return singleton.settings.get(key);
    }

}
