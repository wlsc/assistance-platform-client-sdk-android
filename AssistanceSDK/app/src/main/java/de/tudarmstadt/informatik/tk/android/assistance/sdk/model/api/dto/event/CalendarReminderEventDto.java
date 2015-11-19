package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarReminderEventDto implements SensorDto {

    private Long id;

    @SerializedName("reminderId")
    @Expose
    private Long reminderId;

    @SerializedName("eventId")
    @Expose
    private Long eventId;

    @SerializedName("method")
    @Expose
    private Integer method;

    @SerializedName("minutes")
    @Expose
    private Integer minutes;

    @SerializedName("isNew")
    @Expose
    private Boolean isNew;

    @SerializedName("isUpdated")
    @Expose
    private Boolean isUpdated;

    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public CalendarReminderEventDto() {
        this.type = DtoType.CALENDAR_REMINDER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public CalendarReminderEventDto(Long id) {
        this.id = id;
        this.type = DtoType.CALENDAR_REMINDER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public CalendarReminderEventDto(Long id, Long reminderId, Long eventId, Integer method, Integer minutes, Boolean isNew, Boolean isUpdated, Boolean isDeleted, String created) {
        this.id = id;
        this.reminderId = reminderId;
        this.eventId = eventId;
        this.method = method;
        this.minutes = minutes;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.created = created;
        this.type = DtoType.CALENDAR_REMINDER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
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
        return "CalendarReminderEventDto{" +
                "id=" + id +
                ", reminderId=" + reminderId +
                ", eventId=" + eventId +
                ", method=" + method +
                ", minutes=" + minutes +
                ", isNew=" + isNew +
                ", isUpdated=" + isUpdated +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
