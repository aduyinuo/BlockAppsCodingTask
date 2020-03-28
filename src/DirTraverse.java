/*
 * @author
 * First Name: Yinuo
 * Last Name: Du
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DirTraverse {
    private boolean multiThread;
    private boolean keepAlpha;
    private boolean anyDepth;
    
    private DirTraverse() {
        this.multiThread = false;
    }    
    
    public boolean isKeepAlpha() {
        return keepAlpha;
    }

    public void setKeepAlpha(boolean keepAlpha) {
        this.keepAlpha = keepAlpha;
    }

    public boolean isAnyDepth() {
        return anyDepth;
    }

    public void setAnyDepth(boolean anyDepth) {
        this.anyDepth = anyDepth;
    }

    public boolean isMultiThread() {
        return multiThread;
    }

    public void setMultiThread(boolean multiThread) {
        this.multiThread = multiThread;
    }

    private class TransferThread implements Runnable {
        private BmpTransfer bt;
        private List<List<String>> pairList;

        TransferThread(List<List<String>> pairList, boolean keepAlpha, boolean anyDepth) {
            this.pairList = pairList;
            this.bt = new BmpTransfer();
            this.bt.setKeepAlpha(keepAlpha);
            this.bt.setAnyDepth(anyDepth);
        }

        @Override
        public void run() {
            System.out.println("Thread running");
            for (List<String> pair: this.pairList) {
                System.out.println(pair.get(0)+" "+pair.get(1));
                this.bt.convertPhoto(pair.get(0), pair.get(1));
            }
        }
    }

    // Only consider BMP images
    private List<List<String>> getFileList(String inDir, String outDir) {
        List<List<String>> pairList = new ArrayList<>();
        try {
            Files.walk(Paths.get(inDir))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().toLowerCase().endsWith(".bmp"))
                .forEach(p -> {
                                String inputFile = p.toString();
                                String[] part = inputFile.split("/");
                                String outputFile = outDir + part[part.length - 1];
                                List<String> pair = new ArrayList<String>();
                                pair.add(inputFile);
                                pair.add(outputFile);
                                pairList.add(pair);
                        });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Directory doesn't exist");
            return null;
        }
        return pairList;
    }

    // Single thread implementation
    public void convertFolderSingle(String inDir, String outDir) {
        System.out.println("Running in single thread mode***");
        List<List<String>> pairList = this.getFileList(inDir, outDir);
        if (pairList == null) {
            System.out.println("Input is NULL");
            return;
        }

        TransferThread sr = new TransferThread(pairList, this.keepAlpha, this.anyDepth);
        Thread st = new Thread(sr);
        st.start();

        try {
            st.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Image Saved ............"); 
    }

    // Multiple threads implementation
    public void convertFolderMulti(String inDir, String outDir) {
        System.out.println("Running in multi thread mode***");
        List<List<String>> pairList = this.getFileList(inDir, outDir);
        if (pairList == null) {
            return;
        }

        int idx = 0;
        List<Thread> threads = new ArrayList<Thread>();
        int sed = pairList.size() < 8 ? pairList.size() : pairList.size() / 8;
        while (idx < pairList.size()) {
            int left = idx, right = Math.min(idx + sed, pairList.size());
            List<List<String>> part = pairList.subList(left, right);
            TransferThread sr = new TransferThread(part, this.keepAlpha, this.anyDepth);
            Thread st = new Thread(sr);
            threads.add(st);
            st.start();

            idx = right;
        }

        try {
            for (Thread t: threads) {
                t.join();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("Image Saved ............"); 
    }

    public void convertFolder(String inDir, String outDir) {
        if (!this.multiThread) {
            this.convertFolderSingle(inDir, outDir);
        } else {
            this.convertFolderMulti(inDir, outDir);
        }
    }

    public static void main(String[] args) throws IOException{
        String inDir = "";
        String outDir = "";
        DirTraverse dt = new DirTraverse();

        // Input/Output Directory path are mandatory 
        if (args.length < 2) {
            System.out.println("Please provide input directory and output directory");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == 0) { // First argument: InputDir
                inDir = arg;
                File folder = new File(inDir);
                if (!folder.exists() && !folder.isDirectory()) {
                    System.out.println("Input Dir doesn't exist");
                    return;
                } 
            }
            if (i == 1) { // Second argument: OutputDir
                outDir = arg;
                File folder = new File(outDir);
                if (!folder.exists() && !folder.isDirectory()) {
                    System.out.println("Output Dir doesn't exist");
                    return;
                } 
            }
            if (arg.equals("-noAlpha")) {
                dt.setKeepAlpha(false);
            }
            if (arg.equals("-noDepth")) {
                dt.setAnyDepth(false);
            }
            if (arg.equals("-t8")) {
                dt.setMultiThread(true);
            }
        }

        dt.convertFolder(inDir, outDir);
    }
}
