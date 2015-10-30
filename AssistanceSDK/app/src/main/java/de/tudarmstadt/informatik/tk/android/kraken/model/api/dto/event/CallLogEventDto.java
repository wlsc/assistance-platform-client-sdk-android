package de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28.10.2015
 */
public class CallLogEventDto implements Sensor {

    private Long id;

    @SerializedName("callId")
    @Expose
    private Long callId;

    @SerializedName("type")
    @Expose
    private Integer callType;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("duration")
    @Expose
    private Long duration;

    @SerializedName("new")
    @Expose
    private Boolean isNew;

    @SerializedName("updated")
    @Expose
    private Boolean isUpdated;

    @SerializedName("deleted")
    @Expose
    private Boolean isDeleted;

    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public CallLogEventDto() {
        this.type = DtoType.CALL_LOG;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public CallLogEventDto(Long id) {
        this.id = id;
        this.type = DtoType.CALL_LOG;
        this.typeStr = DtoType.getApiName(this.type);

    }

    public CallLogEventDto(Long id, Long callId, Integer callType, String name, String number, Long date, Long duration, Boolean isNew, Boolean isUpdated, Boolean isDeleted, String created) {
        this.id = id;
        this.callId = callId;
        this.callType = callType;
        this.name = name;
        this.number = number;
        this.date = date;
        this.duration = duration;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.created = created;
        this.type = DtoType.CALL_LOG;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
        return "CallLogEventRequest{" +
                "id=" + id +
                ", callId=" + callId +
                ", callType=" + callType +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", isNew=" + isNew +
                ", isUpdated=" + isUpdated +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
