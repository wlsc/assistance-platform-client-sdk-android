package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.calendar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarReminderEventDto implements SensorDto {

    private Long id;

    @SerializedName("defaultOffset")
    @Expose
    private Boolean defaultOffset;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("offset")
    @Expose
    private Integer offset;

    public CalendarReminderEventDto() {
    }

    public CalendarReminderEventDto(Long id) {
        this.id = id;
    }

    public CalendarReminderEventDto(Long id, Boolean defaultOffset, Integer type, Integer offset) {
        this.id = id;
        this.defaultOffset = defaultOffset;
        this.type = type;
        this.offset = offset;
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public void setType(int type) {
        this.type = -1;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaultOffset() {
        return this.defaultOffset;
    }

    public void setDefaultOffset(Boolean defaultOffset) {
        this.defaultOffset = defaultOffset;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "CalendarReminderEventDto{" +
                "id=" + id +
                ", defaultOffset=" + defaultOffset +
                ", type=" + type +
                ", offset=" + offset +
                '}';
    }
}