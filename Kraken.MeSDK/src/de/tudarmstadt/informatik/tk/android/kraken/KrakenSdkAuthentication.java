package de.tudarmstadt.informatik.tk.android.kraken;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class KrakenSdkAuthentication {

    private static KrakenSdkAuthentication mInstance;

    private final Context mContext;

    public static KrakenSdkAuthentication getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new KrakenSdkAuthentication(context);
        }
        return mInstance;
    }

    public KrakenSdkAuthentication(Context context) {
        mContext = context;
    }

    public void login() {
        Intent krakenLogin = new Intent();
        krakenLogin.setAction(KrakenParams.ACTION_LOGIN);

        if (isKrakenInstalled()) {
            mContext.sendBroadcast(krakenLogin);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder
                    .setMessage("You need to install the Kraken.Me app (Version >= 1.5). Would you like to download it from Google Play?")
                    .setPositiveButton("Go to Google Play", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openKrakenInPlayStore();
                        }
                    })
                    .setNegativeButton("No", null);
            try {
                if(!((Activity)mContext).isFinishing()) {
                    builder.create().show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void logout() {
        Intent krakenKroken = new Intent();
        krakenKroken.setAction(KrakenParams.ACTION_LOGOUT);
        mContext.sendBroadcast(krakenKroken);
    }

    private boolean isKrakenInstalled() {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(KrakenParams.KRAKEN_PACKAGE_NAME,
                    PackageManager.GET_ACTIVITIES);
            if(packageInfo.versionCode < KrakenParams.KRAKEN_MIN_VERSION) {
                return false;
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private void openKrakenInPlayStore() {
        final String appPackageName = KrakenParams.KRAKEN_PACKAGE_NAME;
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}
