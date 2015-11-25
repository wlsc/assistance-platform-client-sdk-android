package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactEmailEventDto implements SensorDto {

    private Long id;

    @SerializedName("contactId")
    @Expose
    private Long contactId;

    @SerializedName("globalContactId")
    @Expose
    private Long globalContactId;

    @SerializedName("mailId")
    @Expose
    private Long mailId;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("eventType")
    @Expose
    private String eventType;

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

    public ContactEmailEventDto() {
        this.type = DtoType.CONTACT_EMAIL;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public ContactEmailEventDto(Long id) {
        this.id = id;
        this.type = DtoType.CONTACT_EMAIL;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public ContactEmailEventDto(Long id, Long globalContactId, Long mailId, String address, String eventType, Boolean isNew, Boolean isUpdated, Boolean isDeleted, Long contactId, String created) {
        this.id = id;
        this.globalContactId = globalContactId;
        this.mailId = mailId;
        this.address = address;
        this.eventType = eventType;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.contactId = contactId;
        this.created = created;
        this.type = DtoType.CONTACT_EMAIL;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGlobalContactId() {
        return globalContactId;
    }

    public void setGlobalContactId(Long globalContactId) {
        this.globalContactId = globalContactId;
    }

    public Long getMailId() {
        return mailId;
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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
        return "ContactEmailEventDto{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", globalContactId=" + globalContactId +
                ", mailId=" + mailId +
                ", address='" + address + '\'' +
                ", eventType='" + eventType + '\'' +
                ", isNew=" + isNew +
                ", isUpdated=" + isUpdated +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
