package com.ldy.werty.rnupdate.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by lidongyang on 2016/7/26.
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    /**
     * 创建文件夹
     *
     * @param dirPath
     * @return
     */
    public static boolean makeFolders(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File f = new File(dirPath);
        return (f.exists() && f.isDirectory()) || f.mkdirs();
    }

    /**
     * 读取Assets文件
     *
     * @param fileName
     * @return 	InputStream
     */
    public static InputStream openAssets(Context context, String fileName) {
        try {
            return context.getAssets().open(fileName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取文本文件
     *
     * @param filePath
     * @return
     */
    public static byte[] readerStreamByte(String filePath) {
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            return readerStreamByte(in);
        } catch (Exception e) {
            Log.e(TAG, "readInStream e[" + e + "]");
        }
        return null;
    }

    public static String readerStreamString(String filePath) {
        try {
            FileInputStream inStream = new FileInputStream(new File(filePath));
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (Exception e) {
            Log.e(TAG, "readInStream e[" + e + "]");
        }
        return null;
    }

    public static byte[] readerStreamByte(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "readInStream e[" + e + "]");
        }
        return null;
    }

    public static boolean writerStreamByte(String filePath, byte[] bytes) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            FileOutputStream outStream = new FileOutputStream(new File(filePath));
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            inputStream.close();
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Log.e(TAG, "readInStream e[" + e + "]");
            return false;
        }
        return true;
    }

    public static boolean writerStreamString(String filePath, String content) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
            FileOutputStream outStream = new FileOutputStream(new File(filePath));
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            inputStream.close();
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Log.e(TAG, "readInStream e[" + e + "]");
            return false;
        }
        return true;
    }


    /**
     * 删除指定目录、文件   以及里面的文件
     *
     * @param dirPath
     * @return
     */
    public static void deleteFile(String dirPath) {
        deleteFile(dirPath, true);
    }

    /**
     * 删除指定目录、文件
     *
     * @param dirPath
     * @param isDelDir 是否删除根目录
     * @return
     */
    public static void deleteFile(String dirPath, boolean isDelDir) {
        if (TextUtils.isEmpty(dirPath)) return;

        File file = new File(dirPath);
        try {
            if (!file.exists())
                return;

            if (file.isFile()) {
                file.delete();
                return;
            }

            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }

            if (isDelDir)
                file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
