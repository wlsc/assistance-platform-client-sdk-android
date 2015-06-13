package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SENSOR_LIGHT.
 */
public class SensorLight implements de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor {

    private Long id;
    private Integer accuracy;
    private Float value;
    private Long timestamp;

    public SensorLight() {
    }

    public SensorLight(Long id) {
        this.id = id;
    }

    public SensorLight(Long id, Integer accuracy, Float value, Long timestamp) {
        this.id = id;
        this.accuracy = accuracy;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}