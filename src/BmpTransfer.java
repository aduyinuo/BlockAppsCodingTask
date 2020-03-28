/*
 * @author
 * First Name: Yinuo
 * Last Name: Du
 */
import org.opencv.core.*;
import org.opencv.imgcodecs.*;


public class BmpTransfer {
    private Imgcodecs imageCodecs;
    private boolean keepAlpha;
    private boolean anyDepth;
    private int readFlag;

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
        System.out.println("Read flag: " + this.readFlag);
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
    }

    public void convertPhoto(String inputFile, String outputFile) {
        // Load negative image 
        Mat negMatrix = this.imageCodecs.imread(inputFile, this.readFlag);
        if (negMatrix == null) {
            System.out.println("BMP read failure");
            return;
        }
        // Meta-date of neg-image
        System.out.println("Image Loaded, channels: " + negMatrix.channels() + ", dim:  " + negMatrix.dims() 
        + ", elemSize1: " + negMatrix.elemSize1() + ", depth: " + negMatrix.depth());

        Size imgSize = negMatrix.size();
        Mat orgMatrix = negMatrix.clone();
        int colorRange = (1 << (negMatrix.elemSize1() * 8)) - 1;
        for (int i = 0; i < imgSize.height; i++) {
            for (int j = 0; j < imgSize.width; j++) {
                double[] data = negMatrix.get(i, j);
                data[0] = colorRange - data[0];
                data[1] = colorRange - data[1];
                data[2] = colorRange - data[2];
                orgMatrix.put(i, j, data);
            }
        }
        this.imageCodecs.imwrite(outputFile, orgMatrix);
    }

    public static void main(String[] args){
         BmpTransfer bt = new BmpTransfer();
         String inputFile = "/Users/yinuo/eclipse-workspace/BmpSaver/images/doggie_out.bmp";
         String outputFile = "/Users/yinuo/eclipse-workspace/BmpSaver/images/doggie_back.bmp";
         bt.convertPhoto(inputFile, outputFile);
    }
}