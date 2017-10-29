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

        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint contour = contours.get(i);
            Double contourArea = contourArea(contour);

            if (contourArea < thresh) {
                /*List<Point> pointsOfContour = contour.toList();
                int xMin = src.cols(), xMax = 0, yMin = src.rows(), yMax = 0;
                for (Point point : pointsOfContour) {
                    if (point.y > yMax) {
                        yMax = (int) point.y;
                    }
                    if (point.y < yMin) {
                        yMin = (int) point.y;
                    }
                    if (point.x > xMax) {
                        xMax = (int) point.x;
                    }
                    if (point.x < xMin) {
                        xMin = (int) point.x;
                    }
                }*/
                /*Rect boundingRectangle = boundingRect(contour);
                int centerX = boundingRectangle.width / 2 + boundingRectangle.x;
                int centerY = boundingRectangle.height / 2 + boundingRectangle.y;*/
                int pixelColor = averageIntensity(src, contour) /*(int) src.get(centerY, centerX)[0]*/;
                //Scalar color = new Scalar(127, 128, 123);
                Scalar color = new Scalar(pixelColor > 127 ? 80 : 167);
                drawContours(dest, contours, i, color, Core.FILLED);
            }
        }

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
