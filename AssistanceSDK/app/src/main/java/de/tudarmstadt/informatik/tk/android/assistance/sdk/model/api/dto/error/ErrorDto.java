package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.12.2015
 */
public class ErrorDto {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("message")
    @Expose
    private String message;

    public ErrorDto(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}