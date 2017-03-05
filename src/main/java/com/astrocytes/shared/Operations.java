package com.astrocytes.shared;

import org.opencv.core.Mat;

/**
 * Created by Nikolay Komarov on 23.02.2017.
 */
public interface Operations {

    public void applyCannyEdgeDetection(Mat image, Integer minThreshold, Integer maxThreshold);

    public void setSourceImage(Mat sourceImage);

    public Mat getOutputImage();

    /**
     * Making dilation and erosion with contours after Canny method applying.
     *
     * @param source     - black and white image
     * @param kernelSize - size of structuring element; must be odd and > 0
     */
    public Mat applyMathMorphology(Mat source, Integer kernelSize);

    /**
     * Converting color image into gray image.
     *
     * @param source - source color image
     * @return grayscale image
     */
    public Mat convertGrayscale(Mat source);

    /**
     * @param source - source white-black image after Canny edge detection
     * @return color image with contours
     */
    public Mat drawAstrocyteCenters(Mat source);

    public void drawLayerDelimiters(Mat source);

}
