package de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class MotionActivityEventRequest implements Sensor {

    private Long id;

    @SerializedName("walking")
    @Expose
    private Boolean walking;

    @SerializedName("running")
    @Expose
    private Boolean running;

    @SerializedName("cycling")
    @Expose
    private Boolean cycling;

    @SerializedName("driving")
    @Expose
    private Boolean driving;

    @SerializedName("stationary")
    @Expose
    private Boolean stationary;

    @SerializedName("unknown")
    @Expose
    private Boolean unknown;

    @SerializedName("accuracy")
    @Expose
    private Integer accuracy;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("onFoot")
    @Expose
    private Boolean onFoot;

    @SerializedName("tilting")
    @Expose
    private Boolean tilting;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public MotionActivityEventRequest() {
    }

    public MotionActivityEventRequest(long id) {
        this.id = id;
    }

    public MotionActivityEventRequest(long id, Boolean walking, Boolean running, Boolean cycling, Boolean driving, Boolean stationary, Boolean unknown, Integer accuracy, String created, Boolean onFoot, Boolean tilting) {
        this.id = id;
        this.walking = walking;
        this.running = running;
        this.cycling = cycling;
        this.driving = driving;
        this.stationary = stationary;
        this.unknown = unknown;
        this.accuracy = accuracy;
        this.created = created;
        this.onFoot = onFoot;
        this.tilting = tilting;
        this.type = SensorType.MOTION_ACTIVITY;
        this.typeStr = SensorType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getWalking() {
        return walking;
    }

    public void setWalking(Boolean walking) {
        this.walking = walking;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Boolean getCycling() {
        return cycling;
    }

    public void setCycling(Boolean cycling) {
        this.cycling = cycling;
    }

    public Boolean getDriving() {
        return driving;
    }

    public void setDriving(Boolean driving) {
        this.driving = driving;
    }

    public Boolean getStationary() {
        return stationary;
    }

    public void setStationary(Boolean stationary) {
        this.stationary = stationary;
    }

    public Boolean getUnknown() {
        return unknown;
    }

    public void setUnknown(Boolean unknown) {
        this.unknown = unknown;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Not-null value.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getOnFoot() {
        return onFoot;
    }

    public void setOnFoot(Boolean onFoot) {
        this.onFoot = onFoot;
    }

    public Boolean getTilting() {
        return tilting;
    }

    public void setTilting(Boolean tilting) {
        this.tilting = tilting;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
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
                "id=" + id +
                ", walking=" + walking +
                ", running=" + running +
                ", cycling=" + cycling +
                ", driving=" + driving +
                ", stationary=" + stationary +
                ", unknown=" + unknown +
                ", accuracy=" + accuracy +
                ", created='" + created + '\'' +
                ", onFoot=" + onFoot +
                ", tilting=" + tilting +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
