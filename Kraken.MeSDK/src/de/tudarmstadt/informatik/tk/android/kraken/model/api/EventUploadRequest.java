package de.tudarmstadt.informatik.tk.android.kraken.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.SensorRequest;

/**
 * Request to assistance server with sensors / events data
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public class EventUploadRequest {

    @SerializedName("device_id")
    @Expose
    private Long serverDeviceId;

    @SerializedName("sensorreadings")
    @Expose
    private List<SensorRequest> events;

    public EventUploadRequest() {
    }

    public EventUploadRequest(Long serverDeviceId, List<SensorRequest> events) {
        this.serverDeviceId = serverDeviceId;
        this.events = events;
    }

    public Long getServerDeviceId() {
        return this.serverDeviceId;
    }

    public void setServerDeviceId(Long serverDeviceId) {
        this.serverDeviceId = serverDeviceId;
    }

    public List<SensorRequest> getEvents() {
        return this.events;
    }

    public void setEvents(List<SensorRequest> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "EventUploadRequest{" +
                "serverDeviceId=" + serverDeviceId +
                ", events=" + events +
                '}';
    }
}
