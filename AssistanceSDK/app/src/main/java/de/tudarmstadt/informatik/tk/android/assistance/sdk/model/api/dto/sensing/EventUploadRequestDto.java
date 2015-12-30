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
    private final Long serverDeviceId;

    @SerializedName("sensorreadings")
    @Expose
    private final List<SensorDto> dataEvents;

    public EventUploadRequestDto(Long serverDeviceId, List<SensorDto> dataEvents) {
        this.serverDeviceId = serverDeviceId;
        this.dataEvents = dataEvents;
    }

    public Long getServerDeviceId() {
        return this.serverDeviceId;
    }

    public List<SensorDto> getDataEvents() {
        return this.dataEvents;
    }

    @Override
    public String toString() {
        return "EventUploadRequestDto{" +
                "serverDeviceId=" + serverDeviceId +
                ", dataEvents=" + dataEvents +
                '}';
    }
}
