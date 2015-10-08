package de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class ConnectionEventRequest implements Sensor {

    private Long id;

    @SerializedName("isWifi")
    @Expose
    private boolean wifi;

    @SerializedName("isMobile")
    @Expose
    private boolean mobile;
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

    public ConnectionEventRequest() {
    }

    public ConnectionEventRequest(long id) {
        this.id = id;
    }

    public ConnectionEventRequest(long id, boolean isWifi, boolean isMobile, String created) {
        this.id = id;
        this.wifi = isWifi;
        this.mobile = isMobile;
        this.created = created;
        this.type = SensorType.CONNECTION;
        this.typeStr = SensorType.getApiName(this.type);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isWifi() {
        return this.wifi;
    }

    public void setIsWifi(boolean isWifi) {
        this.wifi = isWifi;
    }

    public boolean isMobile() {
        return this.mobile;
    }

    public void setIsMobile(boolean isMobile) {
        this.mobile = isMobile;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
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
        return "ConnectionEventRequest{" +
                "id=" + id +
                ", wifi=" + wifi +
                ", mobile=" + mobile +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
