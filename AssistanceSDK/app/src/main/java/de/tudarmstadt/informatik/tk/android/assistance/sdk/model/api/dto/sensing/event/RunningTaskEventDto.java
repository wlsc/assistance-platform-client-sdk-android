package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningTaskEventDto implements SensorDto {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("stackPosition")
    @Expose
    private Integer stackPosition;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public RunningTaskEventDto() {
        this.type = DtoType.RUNNING_TASKS;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningTaskEventDto(String name, Integer stackPosition, String created) {
        this.name = name;
        this.stackPosition = stackPosition;
        this.created = created;
        this.type = DtoType.RUNNING_TASKS;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStackPosition() {
        return stackPosition;
    }

    public void setStackPosition(Integer stackPosition) {
        this.stackPosition = stackPosition;
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
    public String toString() {
        return "RunningTaskEventDto{" +
                ", name='" + name + '\'' +
                ", stackPosition=" + stackPosition +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
