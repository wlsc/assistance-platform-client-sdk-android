package de.tudarmstadt.informatik.tk.android.kraken.models.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class MagneticFieldUncalibratedSensorRequest implements Sensor {

    private long id;

    @SerializedName("xNoHardIron")
    @Expose
    private float xNoHardIron;

    @SerializedName("yNoHardIron")
    @Expose
    private float yNoHardIron;

    @SerializedName("zNoHardIron")
    @Expose
    private float zNoHardIron;

    @SerializedName("xEstimatedIronBias")
    @Expose
    private float xEstimatedIronBias;

    @SerializedName("yEstimatedIronBias")
    @Expose
    private float yEstimatedIronBias;

    @SerializedName("zEstimatedIronBias")
    @Expose
    private float zEstimatedIronBias;

    @SerializedName("created")
    @Expose
    /** Not-null value. */
    private String created;

    private int type;

    public MagneticFieldUncalibratedSensorRequest() {
    }

    public MagneticFieldUncalibratedSensorRequest(long id) {
        this.id = id;
    }

    public MagneticFieldUncalibratedSensorRequest(long id, float xNoHardIron, float yNoHardIron, float zNoHardIron, float xEstimatedIronBias, float yEstimatedIronBias, float zEstimatedIronBias, String created) {
        this.id = id;
        this.xNoHardIron = xNoHardIron;
        this.yNoHardIron = yNoHardIron;
        this.zNoHardIron = zNoHardIron;
        this.xEstimatedIronBias = xEstimatedIronBias;
        this.yEstimatedIronBias = yEstimatedIronBias;
        this.zEstimatedIronBias = zEstimatedIronBias;
        this.created = created;
        this.type = SensorType.MAGNETIC_FIELD_UNCALIBRATED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getXNoHardIron() {
        return xNoHardIron;
    }

    public void setXNoHardIron(float xNoHardIron) {
        this.xNoHardIron = xNoHardIron;
    }

    public float getYNoHardIron() {
        return yNoHardIron;
    }

    public void setYNoHardIron(float yNoHardIron) {
        this.yNoHardIron = yNoHardIron;
    }

    public float getZNoHardIron() {
        return zNoHardIron;
    }

    public void setZNoHardIron(float zNoHardIron) {
        this.zNoHardIron = zNoHardIron;
    }

    public float getXEstimatedIronBias() {
        return xEstimatedIronBias;
    }

    public void setXEstimatedIronBias(float xEstimatedIronBias) {
        this.xEstimatedIronBias = xEstimatedIronBias;
    }

    public float getYEstimatedIronBias() {
        return yEstimatedIronBias;
    }

    public void setYEstimatedIronBias(float yEstimatedIronBias) {
        this.yEstimatedIronBias = yEstimatedIronBias;
    }

    public float getZEstimatedIronBias() {
        return zEstimatedIronBias;
    }

    public void setZEstimatedIronBias(float zEstimatedIronBias) {
        this.zEstimatedIronBias = zEstimatedIronBias;
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
