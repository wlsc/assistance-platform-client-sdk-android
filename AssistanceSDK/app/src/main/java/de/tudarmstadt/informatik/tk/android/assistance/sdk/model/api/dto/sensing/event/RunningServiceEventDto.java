package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningServiceEventDto implements SensorDto {

    private Long id;

    @SerializedName("packageName")
    @Expose
    private String packageName;

    @SerializedName("className")
    @Expose
    private String className;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public RunningServiceEventDto() {
        this.type = DtoType.RUNNING_SERVICES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningServiceEventDto(Long id) {
        this.id = id;
        this.type = DtoType.RUNNING_SERVICES;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public RunningServiceEventDto(Long id, String packageName, String className, String created) {
        this.id = id;
        this.packageName = packageName;
        this.className = className;
        this.created = created;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
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
        return "RunningServiceEventDto{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}