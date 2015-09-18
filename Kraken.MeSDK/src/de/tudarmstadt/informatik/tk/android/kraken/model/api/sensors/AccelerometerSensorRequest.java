package de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.SensorRequest;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class AccelerometerSensorRequest implements SensorRequest {

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

    public AccelerometerSensorRequest() {
    }

    public AccelerometerSensorRequest(long id) {
        this.id = id;
    }

    public AccelerometerSensorRequest(long id, Double x, Double y, Double z, String created, Integer accuracy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.created = created;
        this.accuracy = accuracy;
        this.type = SensorType.ACCELEROMETER;
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

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
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
        return "AccelerometerSensorRequest{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", created='" + created + '\'' +
                ", accuracy=" + accuracy +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
