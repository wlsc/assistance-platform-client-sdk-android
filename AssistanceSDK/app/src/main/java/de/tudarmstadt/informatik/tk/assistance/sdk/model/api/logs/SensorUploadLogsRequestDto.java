package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.logs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public class SensorUploadLogsRequestDto {

    @SerializedName("userId")
    @Expose
    private Long userId;

    @SerializedName("deviceId")
    @Expose
    private Long deviceId;

    @SerializedName("data")
    @Expose
    private List<Data> data;

    public SensorUploadLogsRequestDto() {
    }

    public SensorUploadLogsRequestDto(Long userId, Long deviceId, List<Data> data) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.data = data;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public List<Data> getData() {
        return this.data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SensorUploadLogsRequestDto{" +
                "userId=" + userId +
                ", deviceId=" + deviceId +
                ", data=" + data +
                '}';
    }

    public static class Data {

        @SerializedName("startTime")
        @Expose
        private Long startTime;

        @SerializedName("responseTime")
        @Expose
        private Long responseTime;

        @SerializedName("processingTime")
        @Expose
        private Long processingTime;

        @SerializedName("bodySize")
        @Expose
        private Long bodySize;

        @SerializedName("eventsNumber")
        @Expose
        private Integer eventsNumber;

        @SerializedName("networkType")
        @Expose
        private String networkType;

        public Data() {
        }

        public Data(Long startTime, Long responseTime, Long processingTime, Long bodySize, Integer eventsNumber, String networkType) {
            this.startTime = startTime;
            this.responseTime = responseTime;
            this.processingTime = processingTime;
            this.bodySize = bodySize;
            this.eventsNumber = eventsNumber;
            this.networkType = networkType;
        }

        public Long getStartTime() {
            return this.startTime;
        }

        public Long getResponseTime() {
            return this.responseTime;
        }

        public Long getProcessingTime() {
            return this.processingTime;
        }

        public Long getBodySize() {
            return this.bodySize;
        }

        public Integer getEventsNumber() {
            return this.eventsNumber;
        }

        public String getNetworkType() {
            return this.networkType;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public void setResponseTime(Long responseTime) {
            this.responseTime = responseTime;
        }

        public void setProcessingTime(Long processingTime) {
            this.processingTime = processingTime;
        }

        public void setBodySize(Long bodySize) {
            this.bodySize = bodySize;
        }

        public void setEventsNumber(Integer eventsNumber) {
            this.eventsNumber = eventsNumber;
        }

        public void setNetworkType(String networkType) {
            this.networkType = networkType;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "startTime=" + startTime +
                    ", responseTime=" + responseTime +
                    ", processingTime=" + processingTime +
                    ", bodySize=" + bodySize +
                    ", eventsNumber=" + eventsNumber +
                    ", networkType='" + networkType + '\'' +
                    '}';
        }
    }
}