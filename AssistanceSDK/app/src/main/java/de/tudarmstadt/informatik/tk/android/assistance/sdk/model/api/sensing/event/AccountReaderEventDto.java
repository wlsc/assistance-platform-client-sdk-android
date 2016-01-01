package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class AccountReaderEventDto implements SensorDto {

    @SerializedName("types")
    @Expose
    private String types;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public AccountReaderEventDto() {
        this.type = DtoType.ACCOUNT_READER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public AccountReaderEventDto(String types, String created) {
        this.types = types;
        this.created = created;
        this.type = DtoType.ACCOUNT_READER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "AccountReaderEventDto{" +
                ", types='" + types + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
