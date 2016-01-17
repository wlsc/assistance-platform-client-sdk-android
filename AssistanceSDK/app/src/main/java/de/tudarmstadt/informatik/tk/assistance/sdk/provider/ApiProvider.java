package de.tudarmstadt.informatik.tk.assistance.sdk.provider;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.DeviceApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.ModuleApiProvider;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.01.2016
 */
public class ApiProvider {

    private static final String TAG = ApiProvider.class.getSimpleName();

    private static ApiProvider INSTANCE;

    private Context mContext;

    private ApiProvider(Context context) {
        mContext = context;
    }

    public static ApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     * DeviceApiProvider
     *
     * @return
     */
    public DeviceApiProvider getDeviceApiProvider() {
        return DeviceApiProvider.getInstance(mContext);
    }

    /**
     * ModuleApiProvider
     *
     * @return
     */
    public ModuleApiProvider getModuleApiProvider() {
        return ModuleApiProvider.getInstance(mContext);
    }
}
