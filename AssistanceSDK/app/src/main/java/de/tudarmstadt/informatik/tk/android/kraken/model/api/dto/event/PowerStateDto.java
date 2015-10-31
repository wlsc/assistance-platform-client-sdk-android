package de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateDto implements Sensor {

    private Long id;

    @SerializedName("state")
    @Expose
    private Integer state;

    @SerializedName("isLow")
    @Expose
    private Boolean isLow;

    @SerializedName("isOkay")
    @Expose
    private Boolean isOkay;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public PowerStateDto() {
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public PowerStateDto(Long id) {
        this.id = id;
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public PowerStateDto(Long id, Integer state, Boolean isLow, Boolean isOkay, String created) {
        this.id = id;
        this.state = state;
        this.isLow = isLow;
        this.isOkay = isOkay;
        this.created = created;
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Boolean getIsLow() {
        return this.isLow;
    }

    public void setIsLow(Boolean isLow) {
        this.isLow = isLow;
    }

    public Boolean getIsOkay() {
        return this.isOkay;
    }

    public void setIsOkay(Boolean isOkay) {
        this.isOkay = isOkay;
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
        return this.type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PowerStateDto{" +
                "id=" + id +
                ", state=" + state +
                ", isLow=" + isLow +
                ", isOkay=" + isOkay +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
