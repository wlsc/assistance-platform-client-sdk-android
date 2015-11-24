package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class MotionActivityEventDto implements SensorDto {

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

    public MotionActivityEventDto() {
        this.type = DtoType.MOTION_ACTIVITY;
        this.typeStr = DtoType.getApiName(this.type);

    }

    public MotionActivityEventDto(long id) {
        this.id = id;
        this.type = DtoType.MOTION_ACTIVITY;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public MotionActivityEventDto(long id, Integer walking, Integer running, Integer cycling, Integer driving, Integer stationary, Integer unknown, String created, Integer onFoot, Integer tilting) {
        this.id = id;
        this.walking = walking;
        this.running = running;
        this.cycling = cycling;
        this.driving = driving;
        this.stationary = stationary;
        this.unknown = unknown;
        this.created = created;
        this.onFoot = onFoot;
        this.tilting = tilting;
        this.type = DtoType.MOTION_ACTIVITY;
        this.typeStr = DtoType.getApiName(this.type);
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
                ", created='" + created + '\'' +
                ", onFoot=" + onFoot +
                ", tilting=" + tilting +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
