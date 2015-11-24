package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningServicesEventDto implements SensorDto {

    private Long id;

    @SerializedName("runningServices")
    @Expose
    private String runningServices;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public RunningServicesEventDto() {
        this.type = DtoType.RUNNING_SERVICES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningServicesEventDto(Long id) {
        this.id = id;
        this.type = DtoType.RUNNING_SERVICES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningServicesEventDto(Long id, String runningServices, String created) {
        this.id = id;
        this.runningServices = runningServices;
        this.created = created;
        this.type = DtoType.RUNNING_SERVICES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRunningServices() {
        return runningServices;
    }

    public void setRunningServices(String runningServices) {
        this.runningServices = runningServices;
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
        return "RunningServicesEventDto{" +
                "id=" + id +
                ", runningServices='" + runningServices + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
