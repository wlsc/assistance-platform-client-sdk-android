package de.tudarmstadt.informatik.tk.android.kraken.model.api.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public class DeviceListResponse {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("userId")
    @Expose
    private Long userId;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("messagingRegistrationId")
    @Expose
    private String messagingRegistrationId;

    @SerializedName("userDefinedName")
    @Expose
    private String userDefinedName;

    @SerializedName("lastUsage")
    @Expose
    private Long lastUsage;

    @SerializedName("os")
    @Expose
    private String os;

    @SerializedName("os_version")
    @Expose
    private String osVersion;

    @SerializedName("device_identifier")
    @Expose
    private String deviceIdentifier;

    public DeviceListResponse() {
    }

    public DeviceListResponse(Long id, Long userId, String brand, String model, String messagingRegistrationId, String userDefinedName, Long lastUsage, String os, String osVersion, String deviceIdentifier) {
        this.id = id;
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.messagingRegistrationId = messagingRegistrationId;
        this.userDefinedName = userDefinedName;
        this.lastUsage = lastUsage;
        this.os = os;
        this.osVersion = osVersion;
        this.deviceIdentifier = deviceIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMessagingRegistrationId() {
        return messagingRegistrationId;
    }

    public void setMessagingRegistrationId(String messagingRegistrationId) {
        this.messagingRegistrationId = messagingRegistrationId;
    }

    public String getUserDefinedName() {
        return userDefinedName;
    }

    public void setUserDefinedName(String userDefinedName) {
        this.userDefinedName = userDefinedName;
    }

    public Long getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Long lastUsage) {
        this.lastUsage = lastUsage;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    @Override
    public String toString() {
        return "DeviceListResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", messagingRegistrationId='" + messagingRegistrationId + '\'' +
                ", userDefinedName='" + userDefinedName + '\'' +
                ", lastUsage=" + lastUsage +
                ", os='" + os + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceIdentifier='" + deviceIdentifier + '\'' +
                '}';
    }
}
