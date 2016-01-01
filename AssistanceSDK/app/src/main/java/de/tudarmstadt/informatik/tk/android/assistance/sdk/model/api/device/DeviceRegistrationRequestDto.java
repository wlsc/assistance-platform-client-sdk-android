package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public class DeviceRegistrationRequestDto {

    @SerializedName("device_id")
    @Expose
    private Long deviceId;

    @SerializedName("messaging_registration_id")
    @Expose
    private String registrationToken;

    public DeviceRegistrationRequestDto() {
    }

    public DeviceRegistrationRequestDto(Long deviceId, String registrationToken) {
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
        return "DeviceRegistrationRequestDto{" +
                "deviceId=" + deviceId +
                ", registrationToken='" + registrationToken + '\'' +
                '}';
    }
}
