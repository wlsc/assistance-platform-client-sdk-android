package de.tudarmstadt.informatik.tk.android.kraken.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Karsten Planz
 */
public class ImageUtils {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String getMainColor(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Map<Integer, Integer> m = new HashMap<>();
        for(int i=0; i < width ; i++) {
            for(int j=0; j < height ; j++) {
                int rgb = bitmap.getPixel(i, j);
                int[] rgbArr = getRGBArr(rgb);
                if (!isGray(rgbArr)) {
                    Integer counter = (Integer) m.get(rgb);
                    if (counter == null)
                        counter = 0;
                    counter++;
                    m.put(rgb, counter);
                }
            }
        }
        if(m.size() == 0) {
            return null;
        }
        return getMostCommonColour(m);
    }

    public static String getMostCommonColour(Map<Integer, Integer> map) {
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> lhs, Map.Entry<Integer, Integer> rhs) {
                return lhs.getValue().compareTo(rhs.getValue());
            }
        });
        Map.Entry me = (Map.Entry) list.get(list.size()-1);
        int[] rgb = getRGBArr((Integer)me.getKey());

        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};

    }

    public static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance)
            if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false;
            }
        return true;
    }
}
