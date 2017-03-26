package com.astrocytes.client;

import com.astrocytes.client.data.AppParameters;
import com.astrocytes.client.resources.ClientConstants;
import com.astrocytes.server.OperationsImpl;
import com.astrocytes.shared.Operations;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

/**
 * Created by Nikolay Komarov on 26.03.2017.
 */
public class OperationsExecuter {
    private Operations operations = new OperationsImpl();

    public void setOriginalImage(BufferedImage in) {
        Mat sourceImage = ImageHelper.convertBufferedImageToMat(in);
        operations.setSourceImage(sourceImage);
    }

    public BufferedImage applyCannyEdgeDetection(BufferedImage in) {
        operations.applyCannyEdgeDetection(ImageHelper.convertBufferedImageToMat(in),
                Integer.parseInt((String) AppParameters.getParameter(ClientConstants.CANNY_MIN_THRESH)),
                Integer.parseInt((String) AppParameters.getParameter(ClientConstants.CANNY_MAX_THRESH)));
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public BufferedImage applyDilateAndErode(BufferedImage in) {
        operations.applyMathMorphology(ImageHelper.convertBufferedImageToMat(in),
                Integer.parseInt((String) AppParameters.getParameter(ClientConstants.RADIUS_DIL_ER)));
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public BufferedImage applyGrayScale(BufferedImage in) {
        Mat converted = operations.convertGrayscale(ImageHelper.convertBufferedImageToMat(in));
        return ImageHelper.convertMatToBufferedImage(converted);
    }

    public BufferedImage applyFindAstocytes(BufferedImage in) {
        operations.drawAstrocyteCenters(ImageHelper.convertBufferedImageToMat(in));
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public BufferedImage getCurrentImage() {
        return ImageHelper.convertMatToBufferedImage(operations.getOutputImage());
    }

    public Operations getOperations() {
        return operations;
    }
}
