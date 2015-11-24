package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

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

    @SerializedName("isCharging")
    @Expose
    private Boolean isCharging;

    @SerializedName("percent")
    @Expose
    private Float percent;

    @SerializedName("chargingState")
    @Expose
    private Integer chargingState;

    @SerializedName("chargingMode")
    @Expose
    private Integer chargingMode;

    @SerializedName("powerSaveMode")
    @Expose
    private Boolean powerSaveMode;

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

    public PowerStateEventDto(Long id, Boolean isCharging, Float percent, Integer chargingState, Integer chargingMode, Boolean powerSaveMode, String created) {
        this.id = id;
        this.isCharging = isCharging;
        this.percent = percent;
        this.chargingState = chargingState;
        this.chargingMode = chargingMode;
        this.powerSaveMode = powerSaveMode;
        this.created = created;
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCharging() {
        return isCharging;
    }

    public void setIsCharging(Boolean isCharging) {
        this.isCharging = isCharging;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getChargingState() {
        return chargingState;
    }

    public void setChargingState(Integer chargingState) {
        this.chargingState = chargingState;
    }

    public Integer getChargingMode() {
        return chargingMode;
    }

    public void setChargingMode(Integer chargingMode) {
        this.chargingMode = chargingMode;
    }

    public Boolean getPowerSaveMode() {
        return powerSaveMode;
    }

    public void setPowerSaveMode(Boolean powerSaveMode) {
        this.powerSaveMode = powerSaveMode;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public String toString() {
        return "PowerStateEventDto{" +
                "id=" + id +
                ", isCharging=" + isCharging +
                ", percent=" + percent +
                ", chargingState=" + chargingState +
                ", chargingMode=" + chargingMode +
                ", powerSaveMode=" + powerSaveMode +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
