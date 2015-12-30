package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.social;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Set;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public class FacebookDto implements SensorDto {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("permissions")
    @Expose
    private Set<String> permissions;

    @SerializedName("declinedPermissions")
    @Expose
    private Set<String> declinedPermissions;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public FacebookDto(String token, Set<String> permissions, String created) {
        this.token = token;
        this.permissions = permissions;
        this.declinedPermissions = Collections.emptySet();
        this.created = created;
        this.type = DtoType.SOCIAL_FACEBOOK;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public FacebookDto(String token, Set<String> permissions, Set<String> declinedPermissions, String created) {
        this.token = token;
        this.permissions = permissions;
        this.declinedPermissions = declinedPermissions;
        this.created = created;
        this.type = DtoType.SOCIAL_FACEBOOK;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public String getToken() {
        return this.token;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public Set<String> getDeclinedPermissions() {
        return this.declinedPermissions;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String getCreated() {
        return this.created;
    }

    @Override
    public String toString() {
        return "FacebookDto{" +
                "token='" + token + '\'' +
                ", permissions=" + permissions +
                ", declinedPermissions=" + declinedPermissions +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
