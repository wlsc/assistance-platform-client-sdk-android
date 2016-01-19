package de.tudarmstadt.informatik.tk.assistance.sdk.provider.api;

import android.content.Context;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.device.DeviceApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.device.DeviceListResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.device.DeviceRegistrationRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.device.DeviceUserDefinedNameRequestDto;
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
    public Observable<Void> registerDevice(String userToken, DeviceRegistrationRequestDto deviceRegistrationRequestDto) {
        return api.registerDevice(userToken, deviceRegistrationRequestDto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for device list request
     *
     * @param userToken
     * @return
     */
    public Observable<List<DeviceListResponseDto>> getDeviceList(String userToken) {
        return api.getDeviceList(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for setting user custom device name request
     *
     * @param userToken
     * @return
     */
    public Observable<Void> setUserDefinedName(String userToken,
                                               DeviceUserDefinedNameRequestDto deviceUserDefinedNameRequestDto) {
        return api.setUserDefinedName(userToken, deviceUserDefinedNameRequestDto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}