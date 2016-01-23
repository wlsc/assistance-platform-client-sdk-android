package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public class SensorUploadResponseDto {

    private Long processingTime;

    public SensorUploadResponseDto(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Long getProcessingTime() {
        return this.processingTime;
    }

    @Override
    public String toString() {
        return "SensorUploadResponseDto{" +
                "processingTime=" + processingTime +
                '}';
    }
}