package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class LocationSensorDto implements SensorDto {

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("accuracyHorizontal")
    @Expose
    private Double accuracyHorizontal;

    @SerializedName("speed")
    @Expose
    private Float speed;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("altitude")
    @Expose
    private Double altitude;

    @SerializedName("accuracyVertical")
    @Expose
    private Double accuracyVertical;

    @SerializedName("course")
    @Expose
    private Integer course;

    @SerializedName("floor")
    @Expose
    private Integer floor;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public LocationSensorDto() {
        this.type = SensorApiType.LOCATION;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public LocationSensorDto(Double latitude, Double longitude, Double accuracyHorizontal, Float speed, String created, Double altitude, Double accuracyVertical, Integer course, Integer floor) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracyHorizontal = accuracyHorizontal;
        this.speed = speed;
        this.created = created;
        this.altitude = altitude;
        this.accuracyVertical = accuracyVertical;
        this.course = course;
        this.floor = floor;
        this.type = SensorApiType.LOCATION;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAccuracyHorizontal() {
        return accuracyHorizontal;
    }

    public void setAccuracyHorizontal(Double accuracyHorizontal) {
        this.accuracyHorizontal = accuracyHorizontal;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
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

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAccuracyVertical() {
        return accuracyVertical;
    }

    public void setAccuracyVertical(Double accuracyVertical) {
        this.accuracyVertical = accuracyVertical;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
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
        return "PositionSensorRequest{" +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracyHorizontal=" + accuracyHorizontal +
                ", speed=" + speed +
                ", created='" + created + '\'' +
                ", altitude=" + altitude +
                ", accuracyVertical=" + accuracyVertical +
                ", course=" + course +
                ", floor=" + floor +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
