package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningTasksEventDto implements SensorDto {

    private Long id;

    @SerializedName("runningTasks")
    @Expose
    private String runningTasks;

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

    public RunningTasksEventDto() {
        this.type = DtoType.RUNNING_TASKS;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningTasksEventDto(Long id) {
        this.id = id;
        this.type = DtoType.RUNNING_TASKS;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningTasksEventDto(Long id, String runningTasks, Integer stackPosition, String created) {
        this.id = id;
        this.runningTasks = runningTasks;
        this.stackPosition = stackPosition;
        this.created = created;
        this.type = DtoType.RUNNING_TASKS;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(String runningTasks) {
        this.runningTasks = runningTasks;
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
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RunningTasksEventDto{" +
                "id=" + id +
                ", runningTasks='" + runningTasks + '\'' +
                ", stackPosition=" + stackPosition +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
