package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.external;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public class TucanSensorDto implements SensorDto {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public TucanSensorDto(String username, String password, String created) {
        this.username = username;
        this.password = password;
        this.created = created;
        this.type = SensorApiType.UNI_TUCAN;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String getCreated() {
        return this.created;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }
}