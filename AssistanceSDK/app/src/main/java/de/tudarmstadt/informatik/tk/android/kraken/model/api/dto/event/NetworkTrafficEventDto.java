package de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DTOType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NetworkTrafficEventDto implements Sensor {

    private Long id;

    @SerializedName("appName")
    @Expose
    private String appName;

    @SerializedName("rxBytes")
    @Expose
    private Long rxBytes;

    @SerializedName("txBytes")
    @Expose
    private Long txBytes;

    @SerializedName("background")
    @Expose
    private Boolean background;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public NetworkTrafficEventDto() {
        this.type = DTOType.NETWORK_TRAFFIC;
        this.typeStr = DTOType.getApiName(this.type);
    }

    public NetworkTrafficEventDto(Long id) {
        this.id = id;
        this.type = DTOType.NETWORK_TRAFFIC;
        this.typeStr = DTOType.getApiName(this.type);
    }

    public NetworkTrafficEventDto(Long id, String appName, Long rxBytes, Long txBytes, Boolean background, Double longitude, Double latitude, String created) {
        this.id = id;
        this.appName = appName;
        this.rxBytes = rxBytes;
        this.txBytes = txBytes;
        this.background = background;
        this.longitude = longitude;
        this.latitude = latitude;
        this.created = created;
        this.type = DTOType.NETWORK_TRAFFIC;
        this.typeStr = DTOType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public Long getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(Long txBytes) {
        this.txBytes = txBytes;
    }

    public Boolean getBackground() {
        return background;
    }

    public void setBackground(Boolean background) {
        this.background = background;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NetworkTrafficEventDto{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", rxBytes=" + rxBytes +
                ", txBytes=" + txBytes +
                ", background=" + background +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
