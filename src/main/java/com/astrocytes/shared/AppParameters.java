package com.astrocytes.shared;

import java.util.HashMap;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class AppParameters {

    private HashMap settings;
    public static AppParameters singleton;

    private AppParameters() {
        settings = new HashMap();
    }

    static {
        singleton = new AppParameters();
    }

    public static void setParameter(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.settings.put(key, value);
    }

    public static Object getParameter(String key) {
        return singleton.settings.get(key);
    }
}
