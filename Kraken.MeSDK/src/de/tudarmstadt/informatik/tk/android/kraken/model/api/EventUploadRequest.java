package de.tudarmstadt.informatik.tk.android.kraken.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;

/**
 * Request to assistance server with sensors / dataEvents data
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
    private List<Sensor> dataEvents;

    public EventUploadRequest() {
    }

    public EventUploadRequest(Long serverDeviceId, List<Sensor> dataEvents) {
        this.serverDeviceId = serverDeviceId;
        this.dataEvents = dataEvents;
    }

    public Long getServerDeviceId() {
        return this.serverDeviceId;
    }

    public void setServerDeviceId(Long serverDeviceId) {
        this.serverDeviceId = serverDeviceId;
    }

    public List<Sensor> getDataEvents() {
        return this.dataEvents;
    }

    public void setDataEvents(List<Sensor> dataEvents) {
        this.dataEvents = dataEvents;
    }

    @Override
    public String toString() {
        return "EventUploadRequest{" +
                "serverDeviceId=" + serverDeviceId +
                ", dataEvents=" + dataEvents +
                '}';
    }
}
