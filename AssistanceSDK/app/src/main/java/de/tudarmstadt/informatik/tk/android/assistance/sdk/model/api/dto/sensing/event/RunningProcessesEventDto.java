package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesEventDto implements SensorDto {

    private Long id;

    @SerializedName("runningProcesses")
    @Expose
    private String runningProcesses;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public RunningProcessesEventDto() {
        this.type = DtoType.RUNNING_PROCESSES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningProcessesEventDto(Long id) {
        this.id = id;
        this.type = DtoType.RUNNING_PROCESSES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningProcessesEventDto(Long id, String runningProcesses, String created) {
        this.id = id;
        this.runningProcesses = runningProcesses;
        this.created = created;
        this.type = DtoType.RUNNING_PROCESSES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRunningProcesses() {
        return runningProcesses;
    }

    public void setRunningProcesses(String runningProcesses) {
        this.runningProcesses = runningProcesses;
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
        return "RunningProcessesEventDto{" +
                "id=" + id +
                ", runningProcesses='" + runningProcesses + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
