package de.tudarmstadt.informatik.tk.android.kraken.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "gyroscope_uncalibrated_sensor".
 */
public class GyroscopeUncalibratedSensor implements de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor {

    private Long id;
    private float xNoDrift;
    private float yNoDrift;
    private float zNoDrift;
    private float xEstimatedDrift;
    private float yEstimatedDrift;
    private float zEstimatedDrift;
    /** Not-null value. */
    private String created;

    public GyroscopeUncalibratedSensor() {
    }

    public GyroscopeUncalibratedSensor(Long id) {
        this.id = id;
    }

    public GyroscopeUncalibratedSensor(Long id, float xNoDrift, float yNoDrift, float zNoDrift, float xEstimatedDrift, float yEstimatedDrift, float zEstimatedDrift, String created) {
        this.id = id;
        this.xNoDrift = xNoDrift;
        this.yNoDrift = yNoDrift;
        this.zNoDrift = zNoDrift;
        this.xEstimatedDrift = xEstimatedDrift;
        this.yEstimatedDrift = yEstimatedDrift;
        this.zEstimatedDrift = zEstimatedDrift;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    /** Not-null value. */
    public String getCreated() {
        return created;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreated(String created) {
        this.created = created;
    }

}