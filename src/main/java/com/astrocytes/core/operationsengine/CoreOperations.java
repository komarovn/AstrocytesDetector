package com.astrocytes.core.operationsengine;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

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
        //Core.normalize(src, dest, 50.0, 246.0, Core.NORM_MINMAX);
        //Imgproc.equalizeHist(src, dest);
        return dest;
    }
}
