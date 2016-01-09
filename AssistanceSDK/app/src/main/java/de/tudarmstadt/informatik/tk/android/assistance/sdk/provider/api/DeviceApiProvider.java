package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.api;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device.DeviceApi;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device.DeviceRegistrationRequestDto;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.01.2016
 */
public class DeviceApiProvider {

    private static final String TAG = DeviceApiProvider.class.getSimpleName();

    private static DeviceApiProvider INSTANCE;

    private final DeviceApi api;

    private DeviceApiProvider(Context context) {
        api = ApiGenerator.getInstance(context).create(DeviceApi.class);
    }

    public static DeviceApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new DeviceApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Returns subscription for device registration request
     *
     * @param userToken
     * @param deviceRegistrationRequestDto
     * @return
     */
    public Observable<Void> getDeviceRegistration(String userToken, DeviceRegistrationRequestDto deviceRegistrationRequestDto) {
        return api.registerDevice(userToken, deviceRegistrationRequestDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}