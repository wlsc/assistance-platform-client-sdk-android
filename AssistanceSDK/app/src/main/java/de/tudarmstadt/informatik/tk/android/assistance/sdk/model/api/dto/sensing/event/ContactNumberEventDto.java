package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactNumberEventDto implements SensorDto {

    private Long id;

    @SerializedName("contactId")
    @Expose
    private Long contactId;

    @SerializedName("numberId")
    @Expose
    private Long numberId;

    @SerializedName("eventType")
    @Expose
    private String eventType;

    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("isNew")
    @Expose
    private Boolean isNew;

    @SerializedName("isUpdated")
    @Expose
    private Boolean isUpdated;

    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public ContactNumberEventDto() {
        this.type = DtoType.CONTACT_NUMBER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public ContactNumberEventDto(Long id) {
        this.id = id;
        this.type = DtoType.CONTACT_NUMBER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public ContactNumberEventDto(Long id, Long contactId, Long numberId, String eventType, String number, Boolean isNew, Boolean isUpdated, Boolean isDeleted, String created) {
        this.id = id;
        this.contactId = contactId;
        this.numberId = numberId;
        this.eventType = eventType;
        this.number = number;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.created = created;
        this.type = DtoType.CONTACT_NUMBER;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getNumberId() {
        return numberId;
    }

    public void setNumberId(Long numberId) {
        this.numberId = numberId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
        return "ContactNumberEventDto{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", numberId=" + numberId +
                ", eventType='" + eventType + '\'' +
                ", number='" + number + '\'' +
                ", isNew=" + isNew +
                ", isUpdated=" + isUpdated +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
