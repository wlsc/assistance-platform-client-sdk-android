package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.api.sensors;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class GyroscopeSensorRequest implements Sensor {

    private long id;

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
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GyroscopeSensorRequest{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", created='" + created + '\'' +
                ", type=" + type +
                '}';
    }
}
