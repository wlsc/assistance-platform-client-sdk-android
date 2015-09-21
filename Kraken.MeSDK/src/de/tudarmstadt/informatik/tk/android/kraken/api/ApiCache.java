package de.tudarmstadt.informatik.tk.android.kraken.api;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.kraken.util.FileUtils;

public class ApiCache {

    private static final String CACHE_PATH = "ApiCache";

    private static ApiCache mInstance;
    private final Context mContext;

    public ApiCache(Context context) {
        mContext = context;
    }

    /**
     * Get singleton
     *
     * @param context
     * @return
     */
    public static ApiCache getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ApiCache(context);

        return mInstance;
    }

    public void put(String type, Long from, Long to, String data) {
        if(from == null || to == null) {
            return;
        }
        Calendar now = Calendar.getInstance();
        long[] dayStartEnd = DateUtils.getDayStartEnd(now);
        boolean inPast =  from < dayStartEnd[0];
        if(!inPast) {
            return;
        }

        File cacheFile = getCacheFile(type, from, to);
        if(cacheFile.exists()) {
            return;
        }
        try {
            cacheFile.createNewFile();
            FileUtils.writeFile(cacheFile, data);
            Log.d("kraken-cache", "PUT (" + type + ": " + from + ", " + to + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String type, Long from, Long to) {
        if(from == null || to == null) {
            return null;
        }
        File cacheFile = getCacheFile(type, from, to);
        if(!cacheFile.exists()) {
            Log.d("kraken-cache", "MISS (" + type + ": " + from + ", " + to + ")");
            return null;
        }
        try {
            Log.d("kraken-cache", "HIT (" + type + ": " + from + ", " + to + ")");
            return FileUtils.readFile(cacheFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getCacheFile(String type, Long from, Long to) {
        String hash = hash(type, from, to);
        File cacheDir = new File(mContext.getCacheDir().getPath() + File.separator + CACHE_PATH);
        cacheDir.mkdirs();
        return new File(cacheDir, hash);
    }

    private String hash(String type, Long from, Long to) {
        return sha1(type + String.valueOf(from) + String.valueOf(to));
    };

    /**
     * SHA1 hash
     *
     * @param value
     * @return
     */
    private String sha1(String value) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = value.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
            hash = sb.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
