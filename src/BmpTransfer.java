/*
 * @author
 * First Name: Yinuo
 * Last Name: Du
 */
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;


public class BmpTransfer {
    private Imgcodecs imageCodecs;
    private boolean keepAlpha;
    private boolean anyDepth;
    private int readFlag;
    private Logger logger;

    public void setKeepAlpha(boolean keepAlpha) {
        this.keepAlpha = keepAlpha;
        this.setReadFlag();
    }

    public void setAnyDepth(boolean anyDepth) {
        this.anyDepth = anyDepth;
        this.setReadFlag();
    }

    public void setReadFlag() {
        this.readFlag = 0;
        if (this.keepAlpha) {
            this.readFlag |= Imgcodecs.IMREAD_UNCHANGED;
        } else {
            this.readFlag |= Imgcodecs.IMREAD_COLOR;
        }
        if (this.anyDepth) this.readFlag |= Imgcodecs.IMREAD_ANYDEPTH;
        logger.log(Level.INFO, "Read flag: " + this.readFlag);
    }

    public int getReadFlag() {
        return readFlag;
    }

    public BmpTransfer() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.imageCodecs = new Imgcodecs();
        this.keepAlpha = true; // default save alpha
        this.anyDepth = true; // default save depth

        this.readFlag = 0;
        if (this.keepAlpha) {
            this.readFlag |= Imgcodecs.IMREAD_UNCHANGED;
        } else {
            this.readFlag |= Imgcodecs.IMREAD_COLOR;
        }
        if (this.anyDepth) this.readFlag |= Imgcodecs.IMREAD_ANYDEPTH;

        this.logger = Logger.getLogger(BmpTransfer.class.getName()); 
    }

    public void convertPhoto(String inputFile, String outputFile) {
        // Check input file path existence
        File inDir = new File(inputFile);
        if (!inDir.exists()) {
            logger.log(Level.INFO, "BMP file doesn't exist!");
            return;
        }

        // Load negative image 
        Mat negMatrix = this.imageCodecs.imread(inputFile, this.readFlag);
        if (negMatrix == null) {
            logger.log(Level.INFO, "BMP read failure!");
            return;
        }

        // Meta-data of neg-image
        logger.log(Level.INFO, "Image Loaded, channels: " + negMatrix.channels() + ", dim:  " + negMatrix.dims() 
        + ", elemSize1: " + negMatrix.elemSize1() + ", depth: " + negMatrix.depth());

        Size imgSize = negMatrix.size();
        Mat orgMatrix = negMatrix.clone();

        // calculate original image 
        int colorRange = (1 << (negMatrix.elemSize1() * 8)) - 1;
        for (int i = 0; i < imgSize.height; i++) {
            for (int j = 0; j < imgSize.width; j++) {
                double[] data = negMatrix.get(i, j);
                // Always read in BGR color space
                data[0] = colorRange - data[0];
                data[1] = colorRange - data[1];
                data[2] = colorRange - data[2];
                orgMatrix.put(i, j, data);
            }
        }

        // write to target output path
        this.imageCodecs.imwrite(outputFile, orgMatrix);
    }
}