package de.tudarmstadt.informatik.tk.kraken.android.sdk.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 01.09.2015
 */
public class StorageUtils {

    private StorageUtils() {
    }

    /**
     * Reads content of a cached internal file
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static String readCachedInternalFile(Context context, String filename) throws IOException, FileNotFoundException, UnsupportedEncodingException {

        File folder = context.getCacheDir();
        File file = new File(folder, filename);
        String result = FileUtils.readData(file);

        if (result == null) {
            return "";
        }

        return result;
    }

    /**
     * Reads content of a cached external file
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static String readCachedExternalFile(Context context, String filename) throws IOException, FileNotFoundException, UnsupportedEncodingException {

        File folder = context.getExternalCacheDir();
        File file = new File(folder, filename);
        String result = FileUtils.readData(file);

        if (result == null) {
            return "";
        }

        return result;
    }

    /**
     * Reads content of a private external file
     *
     * @param context
     * @param folderName
     * @param filename
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static String readPrivateExternalFile(Context context, String folderName, String filename) throws IOException, FileNotFoundException, UnsupportedEncodingException {

        File folder = context.getExternalFilesDir(folderName);
        File file = new File(folder, filename);
        String result = FileUtils.readData(file);

        if (result == null) {
            return "";
        }

        return result;
    }

    /**
     * Reads content of a public external file
     *
     * @param folderName
     * @param filename
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static String readPublicExternalFile(String folderName, String filename) throws IOException, FileNotFoundException, UnsupportedEncodingException {

        File folder = Environment.getExternalStoragePublicDirectory(folderName);
        File file = new File(folder, filename);
        String result = FileUtils.readData(file);

        if (result == null) {
            return "";
        }

        return result;
    }

    /**
     * Writes data to internal cache file
     *
     * @param context
     * @param filename
     * @param data
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void saveCachedInternalFile(Context context, String filename, String data) throws IOException, FileNotFoundException, UnsupportedEncodingException {
        File folder = context.getCacheDir();
        File file = new File(folder, filename);
        FileUtils.writeData(file, data);
    }

    /**
     * Writes data to external cache file
     *
     * @param context
     * @param filename
     * @param data
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void saveCachedExternalFile(Context context, String filename, String data) throws IOException, FileNotFoundException, UnsupportedEncodingException {
        File folder = context.getExternalCacheDir();
        File file = new File(folder, filename);
        FileUtils.writeData(file, data);
    }

    /**
     * Writes data to private external file
     *
     * @param context
     * @param folderName
     * @param filename
     * @param data
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void savePrivateExternalFile(Context context, String folderName, String filename, String data) throws IOException, FileNotFoundException, UnsupportedEncodingException {
        File folder = context.getExternalFilesDir(folderName);
        File file = new File(folder, filename);
        FileUtils.writeData(file, data);
    }

    /**
     * Writes data to public external file
     *
     * @param folderName
     * @param filename
     * @param data
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void savePublicExternalFile(String folderName, String filename, String data) throws IOException, FileNotFoundException, UnsupportedEncodingException {
        File folder = Environment.getExternalStoragePublicDirectory(folderName);
        File file = new File(folder, filename);
        FileUtils.writeData(file, data);
    }
}
