package de.tudarmstadt.informatik.tk.assistance.sdk.provider;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.DeviceApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.LoginApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.LogsApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.ModuleApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.SensorUploadApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.UserApiProvider;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.01.2016
 */
public final class ApiProvider {

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
     * LoginApiProvider
     *
     * @return
     */
    public LoginApiProvider getLoginApiProvider() {
        return LoginApiProvider.getInstance(mContext);
    }

    /**
     * UserApiProvider
     *
     * @return
     */
    public UserApiProvider getUserApiProvider() {
        return UserApiProvider.getInstance(mContext);
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

    /**
     * SensorUploadApiProvider
     *
     * @return
     */
    public SensorUploadApiProvider getSensorUploadApiProvider() {
        return SensorUploadApiProvider.getInstance(mContext);
    }

    /**
     * LogsApiProvider
     *
     * @return
     */
    public LogsApiProvider getLogsApiProvider() {
        return LogsApiProvider.getInstance(mContext);
    }
}