package de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ForegroundEvent {

    public static final int EVENT_APP = 0;
    public static final int EVENT_ACTIVITY = 1;
    public static final int EVENT_SCREEN_ON = 2;
    public static final int EVENT_SCREEN_OFF = 3;
    public static final int EVENT_URL = 4;
    public static final int EVENT_KRAKEN_START = 5;
    public static final int EVENT_KRAKEN_STOP = 6;

    private Long id;
    private String title;
    @JsonProperty("package")
    private String packageName;
    private String color;
    private Date start;
    private Date end;
    private long duration;
    private int type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
