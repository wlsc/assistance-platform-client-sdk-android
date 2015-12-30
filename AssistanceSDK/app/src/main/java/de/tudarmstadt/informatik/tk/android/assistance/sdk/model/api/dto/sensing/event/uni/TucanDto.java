package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.uni;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public class TucanDto implements SensorDto {

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

    public TucanDto(String username, String password, String created) {
        this.username = username;
        this.password = password;
        this.created = created;
        this.type = DtoType.UNI_TUCAN;
        this.typeStr = DtoType.getApiName(this.type);
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