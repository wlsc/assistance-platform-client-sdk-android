package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateEventDto implements SensorDto {

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

    public PowerStateEventDto(Boolean isCharging, Float percent, Integer chargingState, Integer chargingMode, Boolean powerSaveMode, String created) {
        this.isCharging = isCharging;
        this.percent = percent;
        this.chargingState = chargingState;
        this.chargingMode = chargingMode;
        this.powerSaveMode = powerSaveMode;
        this.created = created;
        this.type = DtoType.POWER_STATE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Boolean getIsCharging() {
        return this.isCharging;
    }

    public Float getPercent() {
        return this.percent;
    }

    public Integer getChargingState() {
        return this.chargingState;
    }

    public Integer getChargingMode() {
        return this.chargingMode;
    }

    public Boolean getPowerSaveMode() {
        return this.powerSaveMode;
    }

    @Override
    public String getCreated() {
        return this.created;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "PowerStateEventDto{" +
                "isCharging=" + isCharging +
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