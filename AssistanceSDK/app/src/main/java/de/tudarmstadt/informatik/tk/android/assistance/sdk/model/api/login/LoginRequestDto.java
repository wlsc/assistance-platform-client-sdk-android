package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * User login api request
 * <p>
 * Created by Wladimir Schmidt on 28.06.2015.
 */
public class LoginRequestDto {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("device")
    @Expose
    private UserDeviceDto device;

    public LoginRequestDto(String email, String password, UserDeviceDto device) {
        this.email = email;
        this.password = password;
        this.device = device;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public UserDeviceDto getDevice() {
        return this.device;
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", device=" + device +
                '}';
    }
}
