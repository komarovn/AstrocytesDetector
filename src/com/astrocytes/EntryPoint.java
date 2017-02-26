package com.astrocytes;

import java.awt.*;

/**
 * Created by Nikolay Komarov on 24.02.2017.
 */
public class EntryPoint {

    public static void main(String[] argv) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                App app = new App();
            }
        });
    }

}
