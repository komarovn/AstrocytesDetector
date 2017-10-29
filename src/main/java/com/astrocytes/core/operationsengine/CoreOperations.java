package com.astrocytes.core.operationsengine;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class CoreOperations {

    public static Mat invert(Mat src) {
        Mat dest = new Mat();
        Core.bitwise_not(src, dest);
        return dest;
    }

    public static Mat threshold(Mat src, int thresh) {
        Mat dest = new Mat();
        Imgproc.threshold(src, dest, thresh, 255, Imgproc.THRESH_BINARY);
        return dest;
    }

    public static Mat grayscale(Mat src) {
        if (src.channels() < 3) return src;
        Mat dest = new Mat(src.rows(), src.cols(), CvType.CV_8UC1);
        cvtColor(src, dest, COLOR_BGR2GRAY);
        return dest;
    }

    public static Mat erode(Mat src, int radius) {
        Mat dest = new Mat();
        int kernelSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(kernelSize, kernelSize), new Point(radius, radius));
        Imgproc.erode(src, dest, kernel);
        return dest;
    }

    public static Mat dilate(Mat src, int radius) {
        Mat dest = new Mat();
        int kernelSize = radius * 2 + 1;
        Mat kernel = getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(kernelSize, kernelSize), new Point(radius, radius));
        Imgproc.dilate(src, dest, kernel);
        return dest;
    }

    public static Mat xor(Mat first, Mat second) {
        Mat dest = new Mat();
        Core.bitwise_xor(first, second, dest);
        return dest;
    }

    public static Mat and(Mat first, Mat second) {
        Mat dest = new Mat();
        Core.bitwise_and(first, second, dest);
        return dest;
    }

    public static Mat or(Mat first, Mat second) {
        Mat dest = new Mat();
        Core.bitwise_or(first, second, dest);
        return dest;
    }

    public static Mat normalize(Mat src) {
        Mat dest = new Mat();
        Mat ycrcb = new Mat();

        cvtColor(src, ycrcb, Imgproc.COLOR_BGR2YCrCb);

        List<Mat> channels = new ArrayList<Mat>();
        Core.split(ycrcb, channels);

        Imgproc.equalizeHist(channels.get(0), channels.get(0));

        Core.merge(channels, ycrcb);
        cvtColor(ycrcb, dest, Imgproc.COLOR_YCrCb2BGR);
        return dest;
    }

    public static Mat clearContours(Mat src, int thresh) {
        if (src.channels() > 1) return src;

        Mat dest = src.clone();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        findContours(src, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        Mat maskWhite = new Mat(src.rows(), src.cols(), CvType.CV_8UC1, new Scalar(255));
        Mat maskBlack = maskWhite.clone();

        for (int i = 0; i < contours.size(); i++) {
            Double contourArea = contourArea(contours.get(i));

            if (contourArea < thresh) {
                int pixelColor = averageIntensity(src, contours.get(i));
                drawContours(pixelColor > 127 ? maskWhite : maskBlack, contours, i, new Scalar(0), Core.FILLED);
            }
        }

        maskWhite = erode(maskWhite, 2);
        maskBlack = erode(maskBlack, 2);
        dest = and(maskWhite, dest);
        dest = or(invert(maskBlack), dest);

        return dest;
    }

    public static int averageIntensity(Mat src, MatOfPoint contour) {
        int averageIntensityWithinContour = 0;
        int quantityOfPixelsWithinContour = 0;
        Rect boundingRectangle = boundingRect(contour);

        for (int xCoord = (int) boundingRectangle.tl().x; xCoord <= (int) boundingRectangle.br().x; xCoord++) {
            for (int yCoord = (int) boundingRectangle.tl().y; yCoord <= (int) boundingRectangle.br().y; yCoord++) {
                if (pointPolygonTest(new MatOfPoint2f(contour.toArray()), new Point(xCoord, yCoord), false) > 0) {
                    averageIntensityWithinContour += intensity(src, xCoord, yCoord);
                    quantityOfPixelsWithinContour++;
                }
            }
        }

        if (quantityOfPixelsWithinContour == 0) {
            quantityOfPixelsWithinContour = 1;
            averageIntensityWithinContour = intensity(src, boundingRectangle.x, boundingRectangle.y);
            if (src.channels() == 1) {
                averageIntensityWithinContour = averageIntensityWithinContour > 127 ? 0 : 255;
            }
        }

        return averageIntensityWithinContour / quantityOfPixelsWithinContour;
    }

    public static int intensity(Mat src, int x, int y) {
        double[] pixel = src.get(y, x);

        if (pixel == null) return 0;

        if (pixel.length == 1) {
            return (int) pixel[0];
        }

        return (int) (0.11 * pixel[2] + 0.53 * pixel[1] + 0.36 * pixel[0]);
    }
}
