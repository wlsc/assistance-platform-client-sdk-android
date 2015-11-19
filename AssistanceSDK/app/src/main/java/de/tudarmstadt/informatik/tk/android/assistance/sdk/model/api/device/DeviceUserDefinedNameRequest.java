package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public class DeviceUserDefinedNameRequest {

    @SerializedName("device_id")
    @Expose
    private Long deviceId;

    @SerializedName("user_defined_name")
    @Expose
    private String userDefinedName;

    public DeviceUserDefinedNameRequest() {
    }

    public DeviceUserDefinedNameRequest(Long deviceId, String userDefinedName) {
        this.deviceId = deviceId;
        this.userDefinedName = userDefinedName;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserDefinedName() {
        return this.userDefinedName;
    }

    public void setUserDefinedName(String userDefinedName) {
        this.userDefinedName = userDefinedName;
    }

    @Override
    public String toString() {
        return "DeviceUserDefinedNameRequest{" +
                "deviceId=" + deviceId +
                ", userDefinedName='" + userDefinedName + '\'' +
                '}';
    }
}
