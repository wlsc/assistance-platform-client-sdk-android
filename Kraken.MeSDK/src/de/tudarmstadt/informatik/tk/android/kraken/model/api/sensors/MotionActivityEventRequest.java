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

    @SerializedName("accuracy")
    @Expose
    private Integer accuracy;

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

    public MotionActivityEventRequest() {
    }

    public MotionActivityEventRequest(long id) {
        this.id = id;
    }

    public MotionActivityEventRequest(long id, Integer walking, Integer running, Integer cycling, Integer driving, Integer stationary, Integer unknown, Integer accuracy, String created, Integer onFoot, Integer tilting) {
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
        this.type = SensorType.MOTION_ACTIVITY_EVENT;
        this.typeStr = SensorType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
