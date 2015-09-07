package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class GyroscopeUncalibratedSensorRequest implements Sensor {

    private long id;

    @SerializedName("xNoDrift")
    @Expose
    private float xNoDrift;

    @SerializedName("yNoDrift")
    @Expose
    private float yNoDrift;

    @SerializedName("zNoDrift")
    @Expose
    private float zNoDrift;

    @SerializedName("xEstimatedDrift")
    @Expose
    private float xEstimatedDrift;

    @SerializedName("yEstimatedDrift")
    @Expose
    private float yEstimatedDrift;

    @SerializedName("zEstimatedDrift")
    @Expose
    private float zEstimatedDrift;

    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    private int type;

    public GyroscopeUncalibratedSensorRequest() {
    }

    public GyroscopeUncalibratedSensorRequest(long id) {
        this.id = id;
    }

    public GyroscopeUncalibratedSensorRequest(long id, float xNoDrift, float yNoDrift, float zNoDrift, float xEstimatedDrift, float yEstimatedDrift, float zEstimatedDrift, String created) {
        this.id = id;
        this.xNoDrift = xNoDrift;
        this.yNoDrift = yNoDrift;
        this.zNoDrift = zNoDrift;
        this.xEstimatedDrift = xEstimatedDrift;
        this.yEstimatedDrift = yEstimatedDrift;
        this.zEstimatedDrift = zEstimatedDrift;
        this.created = created;
        this.type = SensorType.GYROSCOPE_UNCALIBRATED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getXNoDrift() {
        return xNoDrift;
    }

    public void setXNoDrift(float xNoDrift) {
        this.xNoDrift = xNoDrift;
    }

    public float getYNoDrift() {
        return yNoDrift;
    }

    public void setYNoDrift(float yNoDrift) {
        this.yNoDrift = yNoDrift;
    }

    public float getZNoDrift() {
        return zNoDrift;
    }

    public void setZNoDrift(float zNoDrift) {
        this.zNoDrift = zNoDrift;
    }

    public float getXEstimatedDrift() {
        return xEstimatedDrift;
    }

    public void setXEstimatedDrift(float xEstimatedDrift) {
        this.xEstimatedDrift = xEstimatedDrift;
    }

    public float getYEstimatedDrift() {
        return yEstimatedDrift;
    }

    public void setYEstimatedDrift(float yEstimatedDrift) {
        this.yEstimatedDrift = yEstimatedDrift;
    }

    public float getZEstimatedDrift() {
        return zEstimatedDrift;
    }

    public void setZEstimatedDrift(float zEstimatedDrift) {
        this.zEstimatedDrift = zEstimatedDrift;
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
        return this.type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }
}
