package de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class GyroscopeSensorRequest implements Sensor {

    private Long id;

    @SerializedName("x")
    @Expose
    private Double x;

    @SerializedName("y")
    @Expose
    private Double y;

    @SerializedName("z")
    @Expose
    private Double z;

    @SerializedName("xUncalibratedNoDrift")
    @Expose
    private float xUncalibratedNoDrift;

    @SerializedName("yUncalibratedNoDrift")
    @Expose
    private float yUncalibratedNoDrift;

    @SerializedName("zUncalibratedNoDrift")
    @Expose
    private float zUncalibratedNoDrift;

    @SerializedName("xUncalibratedEstimatedDrift")
    @Expose
    private float xUncalibratedEstimatedDrift;

    @SerializedName("yUncalibratedEstimatedDrift")
    @Expose
    private float yUncalibratedEstimatedDrift;

    @SerializedName("zUncalibratedEstimatedDrift")
    @Expose
    private float zUncalibratedEstimatedDrift;

    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public GyroscopeSensorRequest() {
    }

    public GyroscopeSensorRequest(long id) {
        this.id = id;
    }

    public GyroscopeSensorRequest(long id, Double x, Double y, Double z, String created) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.created = created;
        this.type = SensorType.GYROSCOPE;
        this.typeStr = SensorType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public float getxUncalibratedNoDrift() {
        return this.xUncalibratedNoDrift;
    }

    public void setxUncalibratedNoDrift(float xUncalibratedNoDrift) {
        this.xUncalibratedNoDrift = xUncalibratedNoDrift;
    }

    public float getyUncalibratedNoDrift() {
        return this.yUncalibratedNoDrift;
    }

    public void setyUncalibratedNoDrift(float yUncalibratedNoDrift) {
        this.yUncalibratedNoDrift = yUncalibratedNoDrift;
    }

    public float getzUncalibratedNoDrift() {
        return this.zUncalibratedNoDrift;
    }

    public void setzUncalibratedNoDrift(float zUncalibratedNoDrift) {
        this.zUncalibratedNoDrift = zUncalibratedNoDrift;
    }

    public float getxUncalibratedEstimatedDrift() {
        return this.xUncalibratedEstimatedDrift;
    }

    public void setxUncalibratedEstimatedDrift(float xUncalibratedEstimatedDrift) {
        this.xUncalibratedEstimatedDrift = xUncalibratedEstimatedDrift;
    }

    public float getyUncalibratedEstimatedDrift() {
        return this.yUncalibratedEstimatedDrift;
    }

    public void setyUncalibratedEstimatedDrift(float yUncalibratedEstimatedDrift) {
        this.yUncalibratedEstimatedDrift = yUncalibratedEstimatedDrift;
    }

    public float getzUncalibratedEstimatedDrift() {
        return this.zUncalibratedEstimatedDrift;
    }

    public void setzUncalibratedEstimatedDrift(float zUncalibratedEstimatedDrift) {
        this.zUncalibratedEstimatedDrift = zUncalibratedEstimatedDrift;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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
        return "GyroscopeSensorRequest{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", xUncalibratedNoDrift=" + xUncalibratedNoDrift +
                ", yUncalibratedNoDrift=" + yUncalibratedNoDrift +
                ", zUncalibratedNoDrift=" + zUncalibratedNoDrift +
                ", xUncalibratedEstimatedDrift=" + xUncalibratedEstimatedDrift +
                ", yUncalibratedEstimatedDrift=" + yUncalibratedEstimatedDrift +
                ", zUncalibratedEstimatedDrift=" + zUncalibratedEstimatedDrift +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
