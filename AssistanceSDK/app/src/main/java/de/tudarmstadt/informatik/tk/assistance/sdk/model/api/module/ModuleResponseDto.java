package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Wladimir Schmidt on 30.06.2015.
 */
public class ModuleResponseDto {

    @SerializedName("id")
    @Expose
    private String modulePackage;

    @SerializedName("name")
    @Expose
    private String title;

    @SerializedName("logoUrl")
    @Expose
    private String logo;

    @SerializedName("descriptionShort")
    @Expose
    private String descriptionShort;

    @SerializedName("descriptionLong")
    @Expose
    private String descriptionFull;

    @SerializedName("copyright")
    @Expose
    private String copyright;

    @SerializedName("requiredCapabilities")
    @Expose
    private List<ModuleCapabilityResponseDto> sensorsRequired;

    @SerializedName("optionalCapabilites")
    @Expose
    private List<ModuleCapabilityResponseDto> sensorsOptional;

    @SerializedName("supportEmail")
    @Expose
    private String supportEmail;

    public ModuleResponseDto() {
    }

    public ModuleResponseDto(String modulePackage, String title, String logo, String descriptionShort, String descriptionFull, String copyright, List<ModuleCapabilityResponseDto> sensorsRequired, List<ModuleCapabilityResponseDto> sensorsOptional, String supportEmail) {
        this.modulePackage = modulePackage;
        this.title = title;
        this.logo = logo;
        this.descriptionShort = descriptionShort;
        this.descriptionFull = descriptionFull;
        this.copyright = copyright;
        this.sensorsRequired = sensorsRequired;
        this.sensorsOptional = sensorsOptional;
        this.supportEmail = supportEmail;
    }

    public String getPackageName() {
        return modulePackage;
    }

    public void setModulePackage(String modulePackage) {
        this.modulePackage = modulePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String descriptionFull) {
        this.descriptionFull = descriptionFull;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<ModuleCapabilityResponseDto> getSensorsRequired() {
        return sensorsRequired;
    }

    public void setSensorsRequired(List<ModuleCapabilityResponseDto> sensorsRequired) {
        this.sensorsRequired = sensorsRequired;
    }

    public List<ModuleCapabilityResponseDto> getSensorsOptional() {
        return sensorsOptional;
    }

    public void setSensorsOptional(List<ModuleCapabilityResponseDto> sensorsOptional) {
        this.sensorsOptional = sensorsOptional;
    }

    public String getSupportEmail() {
        return this.supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    @Override
    public String toString() {
        return "ModuleResponseDto{" +
                "modulePackage='" + modulePackage + '\'' +
                ", title='" + title + '\'' +
                ", logo='" + logo + '\'' +
                ", descriptionShort='" + descriptionShort + '\'' +
                ", descriptionFull='" + descriptionFull + '\'' +
                ", copyright='" + copyright + '\'' +
                ", sensorsRequired=" + sensorsRequired +
                ", sensorsOptional=" + sensorsOptional +
                ", supportEmail='" + supportEmail + '\'' +
                '}';
    }
}
