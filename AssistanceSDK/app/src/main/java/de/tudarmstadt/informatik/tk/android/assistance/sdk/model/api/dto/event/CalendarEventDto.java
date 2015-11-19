package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarEventDto implements SensorDto {

    private Long id;

    @SerializedName("eventId")
    @Expose
    private Long eventId;

    @SerializedName("calendarId")
    @Expose
    private Long calendarId;

    @SerializedName("allDay")
    @Expose
    private Boolean allDay;

    @SerializedName("availability")
    @Expose
    private Integer availability;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("timestampStart")
    @Expose
    private Long timestampStart;

    @SerializedName("timestampEnd")
    @Expose
    private Long timestampEnd;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("timezoneStart")
    @Expose
    private String timezoneStart;

    @SerializedName("timezoneEnd")
    @Expose
    private String timezoneEnd;

    @SerializedName("recurrenceExceptionDate")
    @Expose
    private String recurrenceExceptionDate;

    @SerializedName("recurrenceExceptionRule")
    @Expose
    private String recurrenceExceptionRule;

    @SerializedName("hasAlarm")
    @Expose
    private Boolean hasAlarm;

    @SerializedName("lastDate")
    @Expose
    private Long lastDate;

    @SerializedName("originalAllDay")
    @Expose
    private Boolean originalAllDay;

    @SerializedName("originalId")
    @Expose
    private String originalId;

    @SerializedName("originalInstanceTime")
    @Expose
    private Long originalInstanceTime;

    @SerializedName("recurrenceDate")
    @Expose
    private String recurrenceDate;

    @SerializedName("recurrenceRule")
    @Expose
    private String recurrenceRule;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("title")
    @Expose
    private String title;

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

    public CalendarEventDto() {
        this.type = DtoType.CALENDAR;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public CalendarEventDto(Long id) {
        this.id = id;
        this.type = DtoType.CALENDAR;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public CalendarEventDto(Long id, Long eventId, Long calendarId, Boolean allDay, Integer availability, String description, Long timestampStart, Long timestampEnd, String duration, String location, String timezoneStart, String timezoneEnd, String recurrenceExceptionDate, String recurrenceExceptionRule, Boolean hasAlarm, Long lastDate, Boolean originalAllDay, String originalId, Long originalInstanceTime, String recurrenceDate, String recurrenceRule, Integer status, String title, Boolean isNew, Boolean isUpdated, Boolean isDeleted, String created) {
        this.id = id;
        this.eventId = eventId;
        this.calendarId = calendarId;
        this.allDay = allDay;
        this.availability = availability;
        this.description = description;
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
        this.duration = duration;
        this.location = location;
        this.timezoneStart = timezoneStart;
        this.timezoneEnd = timezoneEnd;
        this.recurrenceExceptionDate = recurrenceExceptionDate;
        this.recurrenceExceptionRule = recurrenceExceptionRule;
        this.hasAlarm = hasAlarm;
        this.lastDate = lastDate;
        this.originalAllDay = originalAllDay;
        this.originalId = originalId;
        this.originalInstanceTime = originalInstanceTime;
        this.recurrenceDate = recurrenceDate;
        this.recurrenceRule = recurrenceRule;
        this.status = status;
        this.title = title;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.created = created;
        this.type = DtoType.CALENDAR;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(Long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public Long getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(Long timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimezoneStart() {
        return timezoneStart;
    }

    public void setTimezoneStart(String timezoneStart) {
        this.timezoneStart = timezoneStart;
    }

    public String getTimezoneEnd() {
        return timezoneEnd;
    }

    public void setTimezoneEnd(String timezoneEnd) {
        this.timezoneEnd = timezoneEnd;
    }

    public String getRecurrenceExceptionDate() {
        return recurrenceExceptionDate;
    }

    public void setRecurrenceExceptionDate(String recurrenceExceptionDate) {
        this.recurrenceExceptionDate = recurrenceExceptionDate;
    }

    public String getRecurrenceExceptionRule() {
        return recurrenceExceptionRule;
    }

    public void setRecurrenceExceptionRule(String recurrenceExceptionRule) {
        this.recurrenceExceptionRule = recurrenceExceptionRule;
    }

    public Boolean getHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(Boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public Long getLastDate() {
        return lastDate;
    }

    public void setLastDate(Long lastDate) {
        this.lastDate = lastDate;
    }

    public Boolean getOriginalAllDay() {
        return originalAllDay;
    }

    public void setOriginalAllDay(Boolean originalAllDay) {
        this.originalAllDay = originalAllDay;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public Long getOriginalInstanceTime() {
        return originalInstanceTime;
    }

    public void setOriginalInstanceTime(Long originalInstanceTime) {
        this.originalInstanceTime = originalInstanceTime;
    }

    public String getRecurrenceDate() {
        return recurrenceDate;
    }

    public void setRecurrenceDate(String recurrenceDate) {
        this.recurrenceDate = recurrenceDate;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "CalendarEventDto{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", calendarId=" + calendarId +
                ", allDay=" + allDay +
                ", availability=" + availability +
                ", description='" + description + '\'' +
                ", timestampStart=" + timestampStart +
                ", timestampEnd=" + timestampEnd +
                ", duration='" + duration + '\'' +
                ", location='" + location + '\'' +
                ", timezoneStart='" + timezoneStart + '\'' +
                ", timezoneEnd='" + timezoneEnd + '\'' +
                ", recurrenceExceptionDate='" + recurrenceExceptionDate + '\'' +
                ", recurrenceExceptionRule='" + recurrenceExceptionRule + '\'' +
                ", hasAlarm=" + hasAlarm +
                ", lastDate=" + lastDate +
                ", originalAllDay=" + originalAllDay +
                ", originalId='" + originalId + '\'' +
                ", originalInstanceTime=" + originalInstanceTime +
                ", recurrenceDate='" + recurrenceDate + '\'' +
                ", recurrenceRule='" + recurrenceRule + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", isNew=" + isNew +
                ", isUpdated=" + isUpdated +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
