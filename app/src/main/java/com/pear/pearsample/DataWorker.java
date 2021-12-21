package com.pear.pearsample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Working with files i local directory
 */
public class DataWorker {

    private static String currFolder;
    private final String LOG = "DataWorker: ";

    public DataWorker(final Context context)
    {
        currFolder = context.getApplicationContext().getFilesDir().toString();
    }

    public static boolean isDirEmpty(Context context)
    {
        File cf = Paths.get(currFolder).toFile();
        if (Arrays.stream(cf.listFiles()).count() >0)
        return false;
        else return true;
    }

    public void deleteFile(String name) throws IOException {
        try{
            File [] f1 = getFilesList();
            File file = Paths.get(currFolder + "/" + name).toFile();
            if(file.exists())
            file.delete();
            else throw new IOException("Deleting problem");

            File [] f2 = getFilesList();
            int i =0;
        } catch (IOException e) {
            Log.e(LOG, e.getMessage());
            throw e;
        }
    }


    public File[] getFilesList() {
        File cf = Paths.get(currFolder).toFile();
        return cf.listFiles();
    }

    /**
     * Copy file to local folder. Using Streams as fastest to single copy
     */
    public void copyFileToFolder(String filePath) throws IOException
    {
        Path InputFilePath = Paths.get(filePath);
        InputStream is = null;
        OutputStream os = null;

       File outputFile =  new File(currFolder, InputFilePath.getFileName().toString());

       if (!outputFile.exists()) {
           try {
               is = new FileInputStream(InputFilePath.toFile());
               os = new FileOutputStream(outputFile);
               byte[] buf = new byte[1024];
               int length;
               while ((length = is.read(buf)) > 0) {
                   os.write(buf, 0, length);
               }
           }
           catch (IOException e){
               Log.e(LOG, e.getMessage());
               throw e;
           }
           finally {
               is.close();
               os.close();

           }
       }
       else throw new IOException("File exists.");
    }
}
