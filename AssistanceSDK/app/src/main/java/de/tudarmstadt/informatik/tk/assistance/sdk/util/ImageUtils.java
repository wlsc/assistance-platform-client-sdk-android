package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Karsten Planz
 */
public enum ImageUtils {
    ;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Nullable
    public static String getMainColor(Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bitmap.getPixel(i, j);
                int[] rgbArr = getRGBArr(rgb);
                if (!isGray(rgbArr)) {
                    Integer counter = m.get(rgb);
                    if (counter == null) {
                        counter = 0;
                    }
                    counter++;
                    m.put(rgb, counter);
                }
            }
        }
        if (m.isEmpty()) {
            return null;
        }
        return getMostCommonColour(m);
    }

    public static String getMostCommonColour(Map<Integer, Integer> map) {
        List<Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (lhs, rhs) -> lhs.getValue().compareTo(rhs.getValue()));
        Entry me = (Entry) list.get(list.size() - 1);
        int[] rgb = getRGBArr((Integer) me.getKey());

        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red, green, blue};

    }

    public static boolean isGray(int... rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance) {
            if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false;
            }
        }
        return true;
    }
}
