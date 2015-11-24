package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class AccountReaderEventDto implements SensorDto {

    private Long id;

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

    public AccountReaderEventDto(Long id) {
        this.id = id;
        this.type = DtoType.ACCOUNT_READER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public AccountReaderEventDto(Long id, String types, String created) {
        this.id = id;
        this.types = types;
        this.created = created;
        this.type = DtoType.ACCOUNT_READER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountReaderEventDto{" +
                "id=" + id +
                ", types='" + types + '\'' +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
