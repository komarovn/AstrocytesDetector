package com.astrocytes.server;

import com.astrocytes.shared.Operations;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

/**
 * Created by Nikolay Komarov on 23.02.2017.
 */
public class OperationsImpl implements Operations {

    private Mat sourceImage = new Mat();
    private Mat outputImage;

    private List<Point> astrocytesCenters;

    @Override
    public void applyCannyEdgeDetection(Mat image, Integer minThreshold, Integer maxThreshold) {
        if (minThreshold == null) {
            minThreshold = 0;
        }
        if (maxThreshold == null) {
            maxThreshold = 255;
        }
        Mat dest = convertGrayscale(image);
        Mat copy = new Mat(dest.rows(), dest.cols(), dest.type());
        GaussianBlur(dest, copy, new Size(9, 9), 1.4, 1.4);
        Canny(copy, dest, minThreshold, maxThreshold, 3, true);
        dest.copyTo(getOutputImage());
    }

    @Override
    public void setSourceImage(Mat sourceImage) {
        this.sourceImage = sourceImage;
    }

    @Override
    public Mat getOutputImage() {
        if (outputImage == null) {
            outputImage = new Mat();
            sourceImage.copyTo(outputImage);
        }
        return outputImage;
    }

    @Override
    public Mat applyMathMorphology(Mat source, Integer radius) {
        Mat dest = new Mat();
        int instrumentSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(instrumentSize, instrumentSize), new Point(radius, radius));
        Imgproc.morphologyEx(source, dest, MORPH_CLOSE, kernel, new Point(-1, -1), 1);
        dest.copyTo(getOutputImage());
        return dest;
    }

    @Override
    public Mat convertGrayscale(Mat source) {
        if (source.channels() < 3) {
            source.copyTo(getOutputImage());
            return source;
        }
        Mat dest = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
        cvtColor(source, dest, COLOR_BGR2GRAY);
        dest.copyTo(getOutputImage());
        return dest;
    }

    @Override
    public Mat drawAstrocyteCenters(Mat source) {
        findAstrocytes(source, 30, 20.0, 100);
        if (astrocytesCenters == null) {
            return getOutputImage();
        }
        Mat dest = sourceImage.clone();
        for (Point center : astrocytesCenters) {
            Imgproc.circle(dest, center, 3, new Scalar(108, 240, 3));
        }
        dest.copyTo(getOutputImage());
        return getOutputImage();
    }

    /**
     * Fill array of astrocytes' centers placed on original image.
     * Steps of the algorythm:
     *   1) finding and parameterizing all contours on imege after Canny edge detection;
     *   2) checking contour area;
     *   3) checking size and form of the bounding rectangle;
     *   4) checking for circularity;
     *   5) checking for average intensity within contour.
     *
     * @param source          - image after applying Canny edge detection and morphology operations
     * @param averageRectSize - average size of bounding rectangle
     * @param averageArea     - average area of astrocyte
     * @param intensity       - the value of intensity of the astrocyte center's color
     */
    private void findAstrocytes(Mat source, Integer averageRectSize, Double averageArea, int intensity) {
        if (source.channels() == 3) {
            return;
        }
        astrocytesCenters = new ArrayList<>();

        List<MatOfPoint> contoursAfterFirstIteration = new ArrayList<>();
        Mat hierarchy = new Mat();

        /* Step 1 */
        findContours(source, contoursAfterFirstIteration, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_TC89_L1);

        for (MatOfPoint contour : contoursAfterFirstIteration) {
            Rect boundingRectangle = boundingRect(contour);
            Double contourArea = contourArea(contour);
            Double contourPerimeter = arcLength(new MatOfPoint2f(contour.toArray()), true);
            /* Step 2 */
            if (averageArea - 10 <= contourArea /*&& contourArea <= averageArea + 10*/) {
                int xCoordOfAstrocyteCenter = (int) boundingRectangle.tl().x + boundingRectangle.width / 2;
                int yCoordOfAstrocyteCenter = (int) boundingRectangle.tl().y + boundingRectangle.height / 2;
                astrocytesCenters.add(new Point(xCoordOfAstrocyteCenter, yCoordOfAstrocyteCenter));
                /* Step 3 */
                if (((averageRectSize - 5 <= boundingRectangle.width) && (boundingRectangle.width <= averageRectSize + 5) ||
                        (averageRectSize - 5 <= boundingRectangle.height) && (boundingRectangle.height <= averageRectSize + 5)) &&
                        (boundingRectangle.width / (float) boundingRectangle.height < 1.8f) && (boundingRectangle.height / (float) boundingRectangle.width < 1.8f)) {
                    /* Step 4 */
                    if (contourArea / (contourPerimeter * contourPerimeter) > 0.05 && contourArea / (contourPerimeter * contourPerimeter) < 0.30) {
                        int averageIntensityWithinContour = 0;
                        int quantityOfPixelsWithinContour = 0;
                        for (int xCoord = (int) boundingRectangle.tl().x; xCoord <= (int) boundingRectangle.br().x; xCoord++) {
                            for (int yCoord = (int) boundingRectangle.tl().y; yCoord <= (int) boundingRectangle.br().y; yCoord++) {
                                if (pointPolygonTest(new MatOfPoint2f(contour.toArray()), new Point(xCoord, yCoord), false) > 0) {
                                    averageIntensityWithinContour += calculateIntensity(sourceImage, xCoord, yCoord);
                                    quantityOfPixelsWithinContour++;
                                }
                            }
                        }
                        averageIntensityWithinContour /= quantityOfPixelsWithinContour;
                        /* Step 5 */
                        if (intensity - 30 <= averageIntensityWithinContour && averageIntensityWithinContour <= intensity + 30) {
                            //int xCoordOfAstrocyteCenter = (int) boundingRectangle.tl().x + boundingRectangle.width / 2;
                            //int yCoordOfAstrocyteCenter = (int) boundingRectangle.tl().y + boundingRectangle.height / 2;
                            //astrocytesCenters.add(new Point(xCoordOfAstrocyteCenter, yCoordOfAstrocyteCenter));
                        }
                    }
                }
            }
        }
    }

    private int calculateIntensity(Mat image, int x, int y) {
        double[] pixel = image.get(y, x);
        if (pixel.length == 1) {
            return (int) pixel[0];
        }
        return  (int) (0.11 * pixel[2] + 0.53 * pixel[1] + 0.36 * pixel[0]);
    }

    @Override
    public void drawLayerDelimiters(Mat source) {

    }
}
