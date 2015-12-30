package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class PowerLevelEventDto implements SensorDto {

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

    public PowerLevelEventDto() {
        this.type = DtoType.POWER_LEVEL;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public PowerLevelEventDto(Float percent, String created) {
        this.percent = percent;
        this.created = created;
        this.type = DtoType.POWER_LEVEL;
        this.typeStr = DtoType.getApiName(this.type);
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
    public String toString() {
        return "PowerLevelEventDto{" +
                ", percent=" + percent +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
