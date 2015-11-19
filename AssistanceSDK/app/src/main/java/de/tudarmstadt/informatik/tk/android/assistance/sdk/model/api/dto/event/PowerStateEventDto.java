package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateEventDto implements SensorDto {

    private Long id;

    @SerializedName("charging")
    @Expose
    private Integer typeState;

    @SerializedName("status")
    @Expose
    private Integer chargingStatus;

    @SerializedName("percent")
    @Expose
    private Float percent;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public PowerStateEventDto() {
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public PowerStateEventDto(Long id) {
        this.id = id;
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public PowerStateEventDto(Long id, Integer typeState, Integer chargingStatus, Float percent, String created) {
        this.id = id;
        this.typeState = typeState;
        this.chargingStatus = chargingStatus;
        this.percent = percent;
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

    public Integer getTypeState() {
        return this.typeState;
    }

    public void setTypeState(Integer typeState) {
        this.typeState = typeState;
    }

    public Integer getChargingStatus() {
        return this.chargingStatus;
    }

    public void setChargingStatus(Integer chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public Float getPercent() {
        return this.percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
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
        return "PowerStateEventDto{" +
                "id=" + id +
                ", typeState=" + typeState +
                ", chargingStatus=" + chargingStatus +
                ", percent=" + percent +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
