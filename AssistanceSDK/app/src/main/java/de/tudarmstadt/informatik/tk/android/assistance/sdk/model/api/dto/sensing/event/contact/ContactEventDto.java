package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactEventDto implements SensorDto {

    private Long id;

    @SerializedName("globalContactId")
    @Expose
    private Long globalContactId;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("givenName")
    @Expose
    private String givenName;

    @SerializedName("familyName")
    @Expose
    private String familyName;

    @SerializedName("starred")
    @Expose
    private Integer starred;

    @SerializedName("lastTimeContacted")
    @Expose
    private Integer lastTimeContacted;

    @SerializedName("timesContacted")
    @Expose
    private Integer timesContacted;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("phoneNumbers")
    @Expose
    private Set<ContactEmailNumberItemDto> phoneNumbers;

    @SerializedName("emailAddresses")
    @Expose
    private Set<ContactEmailNumberItemDto> emailAddresses;

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

    public ContactEventDto() {
        this.type = DtoType.CONTACT;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public ContactEventDto(Long id) {
        this.id = id;
        this.type = DtoType.CONTACT;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public ContactEventDto(Long id, Long globalContactId, String displayName, String givenName, String familyName, Integer starred, Integer lastTimeContacted, Integer timesContacted, String note, Set<ContactEmailNumberItemDto> phoneNumbers, Set<ContactEmailNumberItemDto> emailAddresses, Boolean isDeleted, String created) {
        this.id = id;
        this.globalContactId = globalContactId;
        this.displayName = displayName;
        this.givenName = givenName;
        this.familyName = familyName;
        this.starred = starred;
        this.lastTimeContacted = lastTimeContacted;
        this.timesContacted = timesContacted;
        this.note = note;
        this.phoneNumbers = phoneNumbers;
        this.emailAddresses = emailAddresses;
        this.isDeleted = isDeleted;
        this.created = created;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Integer getStarred() {
        return starred;
    }

    public void setStarred(Integer starred) {
        this.starred = starred;
    }

    public Integer getLastTimeContacted() {
        return lastTimeContacted;
    }

    public void setLastTimeContacted(Integer lastTimeContacted) {
        this.lastTimeContacted = lastTimeContacted;
    }

    public Integer getTimesContacted() {
        return timesContacted;
    }

    public void setTimesContacted(Integer timesContacted) {
        this.timesContacted = timesContacted;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<ContactEmailNumberItemDto> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<ContactEmailNumberItemDto> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<ContactEmailNumberItemDto> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<ContactEmailNumberItemDto> emailAddresses) {
        this.emailAddresses = emailAddresses;
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
        return "ContactEventDto{" +
                "id=" + id +
                ", globalContactId=" + globalContactId +
                ", displayName='" + displayName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", starred=" + starred +
                ", lastTimeContacted=" + lastTimeContacted +
                ", timesContacted=" + timesContacted +
                ", note='" + note + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                ", emailAddresses=" + emailAddresses +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}