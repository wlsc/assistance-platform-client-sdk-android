package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RingtoneEventDto implements SensorDto {

    private Long id;

    @SerializedName("mode")
    @Expose
    private Integer mode;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public RingtoneEventDto() {
        this.type = DtoType.RINGTONE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RingtoneEventDto(Long id) {
        this.id = id;
        this.type = DtoType.RINGTONE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RingtoneEventDto(Long id, Integer mode, String created) {
        this.id = id;
        this.mode = mode;
        this.created = created;
        this.type = DtoType.RINGTONE;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
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

    @Override
    public String toString() {
        return "RingtoneEventDto{" +
                "id=" + id +
                ", mode=" + mode +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
