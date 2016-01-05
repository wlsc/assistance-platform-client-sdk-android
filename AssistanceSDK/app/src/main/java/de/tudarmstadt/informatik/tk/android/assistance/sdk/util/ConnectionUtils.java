package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class ConnectionUtils {

    private ConnectionUtils() {
    }


    /**
     * Returns device's network information state
     *
     * @param context
     * @return
     */
    private static NetworkInfo getActiveNetwork(Context context) {

        ConnectivityManager connectivityManager = getConnectivityManager(context);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Supplies a connectivity manager
     *
     * @param context
     * @return
     */
    private static ConnectivityManager getConnectivityManager(Context context) {

        return (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Checks if mobile is connected / active
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {

        NetworkInfo activeNetwork = getActiveNetwork(context);

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if Wi-Fi is connected / active
     *
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context) {

        NetworkInfo activeNetwork = getActiveNetwork(context);

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if device is connected to the internet
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {

        NetworkInfo networkInfo = getActiveNetwork(context);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Checks if airplane mode enabled right now
     *
     * @param context
     * @return
     */
    public static boolean isAirplaneModeEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            return Settings.System.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON,
                    0) == 1;
        } else {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON,
                    0) == 1;
        }
    }

    /**
     * Checks if current connection with the internet is metered (charges apply)
     *
     * @param context
     * @return
     */
    public static boolean isConnectionMetered(Context context) {

        ConnectivityManager manager = getConnectivityManager(context);
        return manager != null && manager.isActiveNetworkMetered();
    }

    /**
     * Gives current network connection type
     *
     * @param context
     * @return
     */
    public static int getConnectionType(Context context) {

        int result = -1;
        NetworkInfo networkInfo = getActiveNetwork(context);
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (networkInfo == null) {
            return -1;
        }

        int networkType = networkInfo.getType();

        switch (networkType) {
            case ConnectivityManager.TYPE_WIFI:
                result = ConnectivityManager.TYPE_WIFI;
                break;
            case ConnectivityManager.TYPE_WIMAX:
                result = ConnectivityManager.TYPE_WIMAX;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        result = TelephonyManager.NETWORK_TYPE_LTE;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        result = TelephonyManager.NETWORK_TYPE_HSPA;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        result = TelephonyManager.NETWORK_TYPE_HSPAP;
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        result = TelephonyManager.NETWORK_TYPE_HSDPA;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        result = TelephonyManager.NETWORK_TYPE_EDGE;
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        result = TelephonyManager.NETWORK_TYPE_GPRS;
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        result = TelephonyManager.NETWORK_TYPE_CDMA;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        result = TelephonyManager.NETWORK_TYPE_UMTS;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        return result;
    }
}
