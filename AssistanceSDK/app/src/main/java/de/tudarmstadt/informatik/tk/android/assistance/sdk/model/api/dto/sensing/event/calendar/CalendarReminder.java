package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.calendar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarReminder implements SensorDto {

    @SerializedName("defaultOffset")
    @Expose
    private Boolean defaultOffset;

    @SerializedName("type")
    @Expose
    private Integer method;

    @SerializedName("offset")
    @Expose
    private Integer offset;

    public CalendarReminder(Boolean defaultOffset, Integer method) {
        this.defaultOffset = defaultOffset;
        this.method = method;
    }

    public CalendarReminder(Boolean defaultOffset, Integer method, Integer offset) {
        this.defaultOffset = defaultOffset;
        this.method = method;
        this.offset = offset;
    }

    public Boolean getDefaultOffset() {
        return this.defaultOffset;
    }

    public Integer getMethod() {
        return this.method;
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public String getCreated() {
        return null;
    }

    public Integer getOffset() {
        return this.offset;
    }

    @Override
    public String toString() {
        return "CalendarReminder{" +
                "defaultOffset=" + defaultOffset +
                ", type=" + method +
                ", offset=" + offset +
                '}';
    }
}