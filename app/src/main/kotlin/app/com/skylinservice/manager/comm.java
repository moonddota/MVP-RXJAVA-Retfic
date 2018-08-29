package app.com.skylinservice.manager;

import android.os.Environment;

import java.io.File;

/**
 * Created by liuxuan on 2018/1/17.
 */

public class comm{
    public static File getPathFile(String path){
        String apkName = path.substring(path.lastIndexOf("/"));
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), apkName);
        return outputFile;
    }

    public static void rmoveFile(String path){
        File file = getPathFile(path);
        file.delete();
    }
}