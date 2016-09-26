package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class MotionActivitySensorDto implements SensorDto {

    @SerializedName("walking")
    @Expose
    private Integer walking;

    @SerializedName("running")
    @Expose
    private Integer running;

    @SerializedName("cycling")
    @Expose
    private Integer cycling;

    @SerializedName("driving")
    @Expose
    private Integer driving;

    @SerializedName("stationary")
    @Expose
    private Integer stationary;

    @SerializedName("unknown")
    @Expose
    private Integer unknown;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("onFoot")
    @Expose
    private Integer onFoot;

    @SerializedName("tilting")
    @Expose
    private Integer tilting;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public MotionActivitySensorDto() {
        this.type = SensorApiType.MOTION_ACTIVITY;
        this.typeStr = SensorApiType.getApiName(this.type);

    }

    public MotionActivitySensorDto(Integer walking, Integer running, Integer cycling, Integer driving, Integer stationary, Integer unknown, String created, Integer onFoot, Integer tilting) {
        this.walking = walking;
        this.running = running;
        this.cycling = cycling;
        this.driving = driving;
        this.stationary = stationary;
        this.unknown = unknown;
        this.created = created;
        this.onFoot = onFoot;
        this.tilting = tilting;
        this.type = SensorApiType.MOTION_ACTIVITY;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public Integer getWalking() {
        return walking;
    }

    public void setWalking(Integer walking) {
        this.walking = walking;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getCycling() {
        return cycling;
    }

    public void setCycling(Integer cycling) {
        this.cycling = cycling;
    }

    public Integer getDriving() {
        return driving;
    }

    public void setDriving(Integer driving) {
        this.driving = driving;
    }

    public Integer getStationary() {
        return stationary;
    }

    public void setStationary(Integer stationary) {
        this.stationary = stationary;
    }

    public Integer getUnknown() {
        return unknown;
    }

    public void setUnknown(Integer unknown) {
        this.unknown = unknown;
    }

    /**
     * Not-null value.
     */
    @Override
    public String getCreated() {
        return created;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getOnFoot() {
        return onFoot;
    }

    public void setOnFoot(Integer onFoot) {
        this.onFoot = onFoot;
    }

    public Integer getTilting() {
        return tilting;
    }

    public void setTilting(Integer tilting) {
        this.tilting = tilting;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public String toString() {
        return "MotionActivityEventRequest{" +
                ", walking=" + walking +
                ", running=" + running +
                ", cycling=" + cycling +
                ", driving=" + driving +
                ", stationary=" + stationary +
                ", unknown=" + unknown +
                ", created='" + created + '\'' +
                ", onFoot=" + onFoot +
                ", tilting=" + tilting +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
