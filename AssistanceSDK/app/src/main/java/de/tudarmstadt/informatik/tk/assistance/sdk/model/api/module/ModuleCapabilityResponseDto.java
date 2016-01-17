package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Capability of a particular assistance module
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 25.08.2015
 */
public class ModuleCapabilityResponseDto {

    @SerializedName("type")
    @Expose
    private String type;

    /* The maximum interval between event readings of this type (in seconds).
    Readings can be cached on the client. (in seconds)
    double; 1.0 = 1 measurement per second, 60.0 = 1 measurement per minute */
    @SerializedName("collection_interval")
    @Expose
    private Double collectionInterval;

    /* The maximum interval in seconds after which the sensor readings
    have to be sent to the platform. -1.0 means that that the readings
    are only sent when the device is connected via WiFi or hasn't sent
    any updates for 24 hours. */
    @SerializedName("update_interval")
    @Expose
    private Double updateInterval;

    /* Only available for the position sensor. 0: low, 1: balanced, 2: best */
    @SerializedName("accuracy")
    @Expose
    private Integer accuracy;

    /* The permissions to request from facebook for this module.
    e.g. ["email", "public_profile", "user_friends"] */
    @SerializedName("permissions")
    @Expose
    private List<String> permissions;

    public ModuleCapabilityResponseDto() {
    }

    public ModuleCapabilityResponseDto(String type, Double collectionInterval, Double updateInterval) {
        this.type = type;
        this.collectionInterval = collectionInterval;
        this.updateInterval = updateInterval;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCollectionInterval() {
        return this.collectionInterval;
    }

    public void setCollectionInterval(Double collectionInterval) {
        this.collectionInterval = collectionInterval;
    }

    public Double getUpdateInterval() {
        return this.updateInterval;
    }

    public void setUpdateInterval(Double updateInterval) {
        this.updateInterval = updateInterval;
    }

    public Integer getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "ModuleCapabilityResponseDto{" +
                "type='" + type + '\'' +
                ", collectionInterval=" + collectionInterval +
                ", updateInterval=" + updateInterval +
                ", accuracy=" + accuracy +
                ", permissions=" + permissions +
                '}';
    }
}
