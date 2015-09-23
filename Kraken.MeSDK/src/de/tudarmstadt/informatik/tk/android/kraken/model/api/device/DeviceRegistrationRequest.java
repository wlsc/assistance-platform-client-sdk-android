package de.tudarmstadt.informatik.tk.android.kraken.model.api.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public class DeviceRegistrationRequest {

    @SerializedName("device_id")
    @Expose
    private Long deviceId;

    @SerializedName("messaging_registration_id")
    @Expose
    private String registrationToken;

    public DeviceRegistrationRequest() {
    }

    public DeviceRegistrationRequest(Long deviceId, String registrationToken) {
        this.deviceId = deviceId;
        this.registrationToken = registrationToken;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegistrationToken() {
        return this.registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    @Override
    public String toString() {
        return "DeviceRegistrationRequest{" +
                "deviceId=" + deviceId +
                ", registrationToken='" + registrationToken + '\'' +
                '}';
    }
}
