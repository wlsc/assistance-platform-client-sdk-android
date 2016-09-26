package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarSensorDto implements SensorDto {

    @SerializedName("eventId")
    @Expose
    private String eventId;

    @SerializedName("calendarId")
    @Expose
    private String calendarId;

    @SerializedName("allDay")
    @Expose
    private Boolean allDay;

    @SerializedName("availability")
    @Expose
    private Integer availability;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("endDate")
    @Expose
    private String endDate;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("recurrenceExceptionDate")
    @Expose
    private String recurrenceExceptionDate;

    @SerializedName("recurrenceExceptionRule")
    @Expose
    private String recurrenceExceptionRule;

    @SerializedName("alarms")
    @Expose
    private Set<CalendarReminder> alarms;

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

    public CalendarSensorDto() {
        this.type = SensorApiType.CALENDAR;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public CalendarSensorDto(String eventId, String calendarId, Boolean allDay, Integer availability, String description, String startDate, String endDate, String duration, String location, String recurrenceExceptionDate, String recurrenceExceptionRule, Set<CalendarReminder> alarms, Long lastDate, Boolean originalAllDay, String originalId, Long originalInstanceTime, String recurrenceDate, String recurrenceRule, Integer status, String title, Boolean isDeleted, String created) {
        this.eventId = eventId;
        this.calendarId = calendarId;
        this.allDay = allDay;
        this.availability = availability;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.location = location;
        this.recurrenceExceptionDate = recurrenceExceptionDate;
        this.recurrenceExceptionRule = recurrenceExceptionRule;
        this.alarms = alarms;
        this.lastDate = lastDate;
        this.originalAllDay = originalAllDay;
        this.originalId = originalId;
        this.originalInstanceTime = originalInstanceTime;
        this.recurrenceDate = recurrenceDate;
        this.recurrenceRule = recurrenceRule;
        this.status = status;
        this.title = title;
        this.isDeleted = isDeleted;
        this.created = created;
        this.type = SensorApiType.CALENDAR;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public Set<CalendarReminder> getAlarms() {
        return alarms;
    }

    public void setAlarms(Set<CalendarReminder> alarms) {
        this.alarms = alarms;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
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
        return "CalendarSensorDto{" +
                ", eventId='" + eventId + '\'' +
                ", calendarId='" + calendarId + '\'' +
                ", allDay=" + allDay +
                ", availability=" + availability +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", duration='" + duration + '\'' +
                ", location='" + location + '\'' +
                ", recurrenceExceptionDate='" + recurrenceExceptionDate + '\'' +
                ", recurrenceExceptionRule='" + recurrenceExceptionRule + '\'' +
                ", alarms=" + alarms +
                ", lastDate=" + lastDate +
                ", originalAllDay=" + originalAllDay +
                ", originalId='" + originalId + '\'' +
                ", originalInstanceTime=" + originalInstanceTime +
                ", recurrenceDate='" + recurrenceDate + '\'' +
                ", recurrenceRule='" + recurrenceRule + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}