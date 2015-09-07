package de.tudarmstadt.informatik.tk.android.kraken.models.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class ConnectionEventRequest implements Sensor {

    private long id;

    @SerializedName("isWifi")
    @Expose
    private boolean isWifi;

    @SerializedName("isMobile")
    @Expose
    private boolean isMobile;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    private int type;

    public ConnectionEventRequest() {
    }

    public ConnectionEventRequest(long id) {
        this.id = id;
    }

    public ConnectionEventRequest(long id, boolean isWifi, boolean isMobile, String created) {
        this.id = id;
        this.isWifi = isWifi;
        this.isMobile = isMobile;
        this.created = created;
        this.type = SensorType.CONNECTION_EVENT;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsWifi() {
        return isWifi;
    }

    public void setIsWifi(boolean isWifi) {
        this.isWifi = isWifi;
    }

    public boolean getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(boolean isMobile) {
        this.isMobile = isMobile;
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
}
