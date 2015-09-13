package de.tudarmstadt.informatik.tk.android.kraken.models.api.device;

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
    private String registrationId;

    public DeviceRegistrationRequest() {
    }

    public DeviceRegistrationRequest(Long deviceId, String registrationId) {
        this.deviceId = deviceId;
        this.registrationId = registrationId;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public String toString() {
        return "DeviceRegistrationRequest{" +
                "deviceId=" + deviceId +
                ", registrationId='" + registrationId + '\'' +
                '}';
    }
}
