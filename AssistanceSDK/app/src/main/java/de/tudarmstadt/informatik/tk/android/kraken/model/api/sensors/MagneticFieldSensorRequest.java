package de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class MagneticFieldSensorRequest implements Sensor {

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

    @SerializedName("xUncalibratedNoHardIron")
    @Expose
    private float xUncalibratedNoHardIron;

    @SerializedName("yUncalibratedNoHardIron")
    @Expose
    private float yUncalibratedNoHardIron;

    @SerializedName("zUncalibratedNoHardIron")
    @Expose
    private float zUncalibratedNoHardIron;

    @SerializedName("xUncalibratedEstimatedIronBias")
    @Expose
    private float xUncalibratedEstimatedIronBias;

    @SerializedName("yUncalibratedEstimatedIronBias")
    @Expose
    private float yUncalibratedEstimatedIronBias;

    @SerializedName("zUncalibratedEstimatedIronBias")
    @Expose
    private float zUncalibratedEstimatedIronBias;

    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("accuracy")
    @Expose
    private Integer accuracy;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public MagneticFieldSensorRequest() {
    }

    public MagneticFieldSensorRequest(long id) {
        this.id = id;
    }

    public MagneticFieldSensorRequest(long id, Double x, Double y, Double z, String created, Integer accuracy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.created = created;
        this.accuracy = accuracy;
        this.type = SensorType.MAGNETIC_FIELD;
        this.typeStr = SensorType.getApiName(this.type);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return this.z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public float getxUncalibratedNoHardIron() {
        return this.xUncalibratedNoHardIron;
    }

    public void setxUncalibratedNoHardIron(float xUncalibratedNoHardIron) {
        this.xUncalibratedNoHardIron = xUncalibratedNoHardIron;
    }

    public float getyUncalibratedNoHardIron() {
        return this.yUncalibratedNoHardIron;
    }

    public void setyUncalibratedNoHardIron(float yUncalibratedNoHardIron) {
        this.yUncalibratedNoHardIron = yUncalibratedNoHardIron;
    }

    public float getzUncalibratedNoHardIron() {
        return this.zUncalibratedNoHardIron;
    }

    public void setzUncalibratedNoHardIron(float zUncalibratedNoHardIron) {
        this.zUncalibratedNoHardIron = zUncalibratedNoHardIron;
    }

    public float getxUncalibratedEstimatedIronBias() {
        return this.xUncalibratedEstimatedIronBias;
    }

    public void setxUncalibratedEstimatedIronBias(float xUncalibratedEstimatedIronBias) {
        this.xUncalibratedEstimatedIronBias = xUncalibratedEstimatedIronBias;
    }

    public float getyUncalibratedEstimatedIronBias() {
        return this.yUncalibratedEstimatedIronBias;
    }

    public void setyUncalibratedEstimatedIronBias(float yUncalibratedEstimatedIronBias) {
        this.yUncalibratedEstimatedIronBias = yUncalibratedEstimatedIronBias;
    }

    public float getzUncalibratedEstimatedIronBias() {
        return this.zUncalibratedEstimatedIronBias;
    }

    public void setzUncalibratedEstimatedIronBias(float zUncalibratedEstimatedIronBias) {
        this.zUncalibratedEstimatedIronBias = zUncalibratedEstimatedIronBias;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MagneticFieldSensorRequest{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", xUncalibratedNoHardIron=" + xUncalibratedNoHardIron +
                ", yUncalibratedNoHardIron=" + yUncalibratedNoHardIron +
                ", zUncalibratedNoHardIron=" + zUncalibratedNoHardIron +
                ", xUncalibratedEstimatedIronBias=" + xUncalibratedEstimatedIronBias +
                ", yUncalibratedEstimatedIronBias=" + yUncalibratedEstimatedIronBias +
                ", zUncalibratedEstimatedIronBias=" + zUncalibratedEstimatedIronBias +
                ", created='" + created + '\'' +
                ", accuracy=" + accuracy +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
