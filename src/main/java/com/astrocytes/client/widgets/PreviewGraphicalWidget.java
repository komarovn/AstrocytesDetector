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

    private BufferedImage originalImage;
    private Integer widthWidget;
    private Integer heightWidget;

    private MouseAdapter mouseAdapter;

    public PreviewGraphicalWidget(Integer width, Integer height) {
        super(width, height);
        widthWidget = width == null ? Integer.parseInt(ClientConstants.PREVIEW_WINDOW_WIDTH) : width;
        heightWidget = height == null ? Integer.parseInt(ClientConstants.PREVIEW_WINDOW_HEIGHT) : height;
        updateWidget(width, height);
        setZoomEnabled(false);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        addListenerForPreview();
    }

    public PreviewGraphicalWidget(Integer width, Integer height, BufferedImage originalImage) {
        this(width, height);
        this.originalImage = originalImage;
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

    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    public BufferedImage getOriginalImageView() {
        BufferedImage originalImageView = originalImage.getSubimage(currentX, currentY, widthImage, heightImage);
        BufferedImage originalImageViewCropped = new BufferedImage(originalImageView.getColorModel(),
                originalImageView.getRaster().createCompatibleWritableRaster(originalImageView.getWidth(), originalImageView.getHeight()),
                originalImageView.isAlphaPremultiplied(), null);
        originalImageView.copyData(originalImageViewCropped.getRaster());
        return originalImageViewCropped;
    }

}
