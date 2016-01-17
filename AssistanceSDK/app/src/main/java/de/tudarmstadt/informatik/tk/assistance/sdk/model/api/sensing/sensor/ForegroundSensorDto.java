package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * Foreground event
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class ForegroundSensorDto implements SensorDto {

    @SerializedName("packageName")
    @Expose
    private String packageName;

    @SerializedName("appName")
    @Expose
    private String appName;

    @SerializedName("className")
    @Expose
    private String className;

    @SerializedName("activityLabel")
    @Expose
    private String activityLabel;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("eventType")
    @Expose
    private String eventType;

    @SerializedName("keystrokes")
    @Expose
    private Integer keystrokes;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public ForegroundSensorDto() {
        this.type = SensorApiType.FOREGROUND;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public ForegroundSensorDto(String packageName, String appName, String className, String activityLabel, String color, String url, String eventType, Integer keystrokes, String created) {
        this.packageName = packageName;
        this.appName = appName;
        this.className = className;
        this.activityLabel = activityLabel;
        this.color = color;
        this.url = url;
        this.eventType = eventType;
        this.keystrokes = keystrokes;
        this.created = created;
        this.type = SensorApiType.FOREGROUND;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    @Override
    public int getType() {
        return this.type;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getActivityLabel() {
        return activityLabel;
    }

    public void setActivityLabel(String activityLabel) {
        this.activityLabel = activityLabel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getKeystrokes() {
        return keystrokes;
    }

    public void setKeystrokes(Integer keystrokes) {
        this.keystrokes = keystrokes;
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
    public String toString() {
        return "ForegroundEventRequest{" +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", className='" + className + '\'' +
                ", activityLabel='" + activityLabel + '\'' +
                ", color='" + color + '\'' +
                ", url='" + url + '\'' +
                ", eventType='" + eventType + '\'' +
                ", keystrokes='" + keystrokes + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
