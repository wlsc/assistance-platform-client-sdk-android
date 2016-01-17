package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class ConnectionSensorDto implements SensorDto {

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

    public ConnectionSensorDto() {
        this.type = SensorApiType.CONNECTION;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public ConnectionSensorDto(boolean isWifi, boolean isMobile, String created) {
        this.wifi = isWifi;
        this.mobile = isMobile;
        this.created = created;
        this.type = SensorApiType.CONNECTION;
        this.typeStr = SensorApiType.getApiName(this.type);
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
    public String toString() {
        return "ConnectionEventRequest{" +
                ", wifi=" + wifi +
                ", mobile=" + mobile +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
