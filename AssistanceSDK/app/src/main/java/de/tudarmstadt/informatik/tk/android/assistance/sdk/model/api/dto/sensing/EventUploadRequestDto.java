package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * Request to assistance server with sensors / dataEvents data
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public class EventUploadRequestDto {

    @SerializedName("device_id")
    @Expose
    private Long serverDeviceId;

    @SerializedName("sensorreadings")
    @Expose
    private List<SensorDto> dataEvents;

    public EventUploadRequestDto() {
    }

    public EventUploadRequestDto(Long serverDeviceId, List<SensorDto> dataEvents) {
        this.serverDeviceId = serverDeviceId;
        this.dataEvents = dataEvents;
    }

    public Long getServerDeviceId() {
        return this.serverDeviceId;
    }

    public void setServerDeviceId(Long serverDeviceId) {
        this.serverDeviceId = serverDeviceId;
    }

    public List<SensorDto> getDataEvents() {
        return this.dataEvents;
    }

    public void setDataEvents(List<SensorDto> dataEvents) {
        this.dataEvents = dataEvents;
    }

    @Override
    public String toString() {
        return "EventUploadRequestDto{" +
                "serverDeviceId=" + serverDeviceId +
                ", dataEvents=" + dataEvents +
                '}';
    }
}