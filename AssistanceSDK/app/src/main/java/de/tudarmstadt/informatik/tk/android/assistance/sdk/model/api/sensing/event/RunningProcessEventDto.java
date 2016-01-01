package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessEventDto implements SensorDto {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public RunningProcessEventDto() {
        this.type = DtoType.RUNNING_PROCESSES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningProcessEventDto(String name, String created) {
        this.name = name;
        this.created = created;
        this.type = DtoType.RUNNING_PROCESSES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "RunningProcessEventDto{" +
                ", name='" + name + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
