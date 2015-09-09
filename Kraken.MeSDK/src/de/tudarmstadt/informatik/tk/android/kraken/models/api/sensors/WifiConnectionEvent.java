package de.tudarmstadt.informatik.tk.android.kraken.models.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class WifiConnectionEvent implements Sensor {

    private Long id;

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

    private int type;

    public WifiConnectionEvent() {
    }

    public WifiConnectionEvent(long id) {
        this.id = id;
    }

    public WifiConnectionEvent(long id, String ssid, String bssid, String created, Integer channel, Integer frequency, Integer linkSpeed, Integer signalStrength, Integer networkId) {
        this.id = id;
        this.ssid = ssid;
        this.bssid = bssid;
        this.created = created;
        this.channel = channel;
        this.frequency = frequency;
        this.linkSpeed = linkSpeed;
        this.signalStrength = signalStrength;
        this.networkId = networkId;
        this.type = SensorType.WIFI_CONNECTION_EVENT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WifiConnectionEvent{" +
                "id=" + id +
                ", ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", created='" + created + '\'' +
                ", channel=" + channel +
                ", frequency=" + frequency +
                ", linkSpeed=" + linkSpeed +
                ", signalStrength=" + signalStrength +
                ", networkId=" + networkId +
                ", type=" + type +
                '}';
    }
}
