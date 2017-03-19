package com.astrocytes.client;

import com.astrocytes.shared.Operations;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Nikolay Komarov on 23.02.2017.
 */
public abstract class ImageHelper {

    public static BufferedImage convertMatToBufferedImage(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[in.cols() * in.rows() * (int) in.elemSize()];
        in.get(0, 0, data);
        int type = BufferedImage.TYPE_3BYTE_BGR;
        switch (in.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb
                byte b;
                for(int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
        }
        out = new BufferedImage(in.cols(), in.rows(), type);
        out.getRaster().setDataElements(0, 0, in.cols(), in.rows(), data);
        return out;
    }

    public static Mat convertBufferedImageToMat(BufferedImage in) {
        byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
        int type = CvType.CV_8UC3;
        if (in.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            type = CvType.CV_8UC1;
        }
        Mat out = new Mat(in.getHeight(), in.getWidth(), type);
        out.put(0, 0, pixels);
        return out;
    }

    public static BufferedImage cloneBufferedImage(BufferedImage in) {
        ColorModel cm = in.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = in.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void saveImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage loadImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
