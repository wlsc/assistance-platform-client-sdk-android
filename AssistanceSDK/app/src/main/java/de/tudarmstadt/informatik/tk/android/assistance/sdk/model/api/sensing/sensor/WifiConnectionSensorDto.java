package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class WifiConnectionSensorDto implements SensorDto {

    @SerializedName("ssid")
    @Expose
    private String ssid;

    @SerializedName("bssid")
    @Expose
    private String bssid;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("channel")
    @Expose
    private Integer channel;

    @SerializedName("frequency")
    @Expose
    private Integer frequency;

    @SerializedName("linkSpeed")
    @Expose
    private Integer linkSpeed;

    @SerializedName("signalStrength")
    @Expose
    private Integer signalStrength;

    @SerializedName("networkId")
    @Expose
    private Integer networkId;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public WifiConnectionSensorDto() {
        this.type = SensorApiType.WIFI_CONNECTION;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public WifiConnectionSensorDto(String ssid, String bssid, String created, Integer channel, Integer frequency, Integer linkSpeed, Integer signalStrength, Integer networkId) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.created = created;
        this.channel = channel;
        this.frequency = frequency;
        this.linkSpeed = linkSpeed;
        this.signalStrength = signalStrength;
        this.networkId = networkId;
        this.type = SensorApiType.WIFI_CONNECTION;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
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

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getLinkSpeed() {
        return linkSpeed;
    }

    public void setLinkSpeed(Integer linkSpeed) {
        this.linkSpeed = linkSpeed;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public String toString() {
        return "WifiConnectionEvent{" +
                ", ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", created='" + created + '\'' +
                ", channel=" + channel +
                ", frequency=" + frequency +
                ", linkSpeed=" + linkSpeed +
                ", signalStrength=" + signalStrength +
                ", networkId=" + networkId +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
