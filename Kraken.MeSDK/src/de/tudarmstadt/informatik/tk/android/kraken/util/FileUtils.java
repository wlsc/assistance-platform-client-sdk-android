package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class FileUtils {

    /**
     * Checks if external storage is available for read and write.
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getPrivateFilesDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return context.getExternalFilesDir(null);
        } else {
            String packageName = context.getPackageName();
            File externalPath = Environment.getExternalStorageDirectory();
            File appFiles = new File(externalPath.getAbsolutePath() +
                    "/Android/data/" + packageName + "/files");
            appFiles.mkdirs();
            return appFiles;
        }
    }

    /**
     * Reads a file and returns the contents as String.
     *
     * @param stream the InputStream from the file
     * @return a String with the file contents
     * @throws IOException
     */
    public static String readFile(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * Reads a file and returns the contents as String.
     *
     * @param f the File object
     * @return a String with the file contents.
     * @throws IOException
     */
    public static String readFile(File f) throws IOException {
        FileInputStream stream = new FileInputStream(f);
        return readFile(stream);
    }

    /**
     * Reads file into a string
     *
     * @param file
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static String readData(File file) throws IOException, FileNotFoundException, UnsupportedEncodingException {

        FileInputStream fileInputStream = null;

        try {

            fileInputStream = new FileInputStream(file);
            StringBuffer sb = new StringBuffer();
            int read = -1;

            while ((read = fileInputStream.read()) != -1) {
                sb.append((char) read);
            }

            return sb.toString();
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    /**
     * Writes a file with the given content.
     *
     * @param f    the File object
     * @param text the contents
     * @throws IOException
     */
    public static void writeFile(File f, String text) throws IOException {
        final String content = new String(text);
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        osw.write(content);
        osw.close();
    }

    /**
     * Write data to given file
     *
     * @param file
     * @param data
     */
    public static void writeData(File file, String data) throws IOException, FileNotFoundException, UnsupportedEncodingException {

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] bytes = data.getBytes("UTF-8");
            fileOutputStream.write(bytes);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
