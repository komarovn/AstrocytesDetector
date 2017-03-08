package com.astrocytes.client.widgets;

import com.astrocytes.client.resources.ClientConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Nikolay Komarov on 08.03.2017.
 */
public abstract class PreviewGraphicalWidget extends GraphicalWidget {

    private Integer widthWidget;
    private Integer heightWidget;

    private MouseAdapter mouseAdapter;

    public PreviewGraphicalWidget(Integer width, Integer height) {
        super(width, height);
        widthWidget = width == null ? ClientConstants.PREVIEW_WINDOW_WIDTH : width;
        heightWidget = height == null ? ClientConstants.PREVIEW_WINDOW_HEIGHT : height;
        updateWidget(width, height);
        setZoomEnabled(false);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        addListenerForPreview();
    }

    private void addListenerForPreview() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (currentView != null) {
                    processPreviewImage();
                }
            }
        };
        addMouseListener(mouseAdapter);
    }

    public abstract void processPreviewImage();

    public void updatePreview(BufferedImage newCurrentView) {
        setCurrentView(newCurrentView);
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

}
