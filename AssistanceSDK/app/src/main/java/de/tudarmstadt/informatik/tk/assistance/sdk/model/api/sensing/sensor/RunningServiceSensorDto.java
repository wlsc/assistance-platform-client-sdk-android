package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningServiceSensorDto implements SensorDto {

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

    public RunningServiceSensorDto() {
        this.type = SensorApiType.RUNNING_SERVICES;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public RunningServiceSensorDto(String packageName, String className, String created) {
        this.packageName = packageName;
        this.className = className;
        this.created = created;
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

    @Override
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
        return "RunningServiceSensorDto{" +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}