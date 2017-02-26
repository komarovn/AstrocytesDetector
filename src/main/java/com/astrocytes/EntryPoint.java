package com.astrocytes;

import org.opencv.core.Core;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nikolay Komarov on 24.02.2017.
 */
public class EntryPoint {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
    }

    public static void main(String[] argv) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                App app = new App();
            }
        });
    }

}
