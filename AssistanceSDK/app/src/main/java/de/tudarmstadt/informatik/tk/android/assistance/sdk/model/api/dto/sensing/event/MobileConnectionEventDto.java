package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class MobileConnectionEventDto implements SensorDto {

    @SerializedName("carrierName")
    @Expose
    private String carrierName;

    @SerializedName("mobileCountryCode")
    @Expose
    private String mobileCountryCode;

    @SerializedName("mobileNetworkCode")
    @Expose
    private String mobileNetworkCode;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("voipAvailable")
    @Expose
    private Boolean voipAvailable;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public MobileConnectionEventDto() {
        this.type = DtoType.MOBILE_DATA_CONNECTION;
        this.typeStr = DtoType.getApiName(this.type);

    }

    public MobileConnectionEventDto(String carrierName, String mobileCountryCode, String mobileNetworkCode, String created, Boolean voipAvailable) {
        this.carrierName = carrierName;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNetworkCode = mobileNetworkCode;
        this.created = created;
        this.voipAvailable = voipAvailable;
        this.type = DtoType.MOBILE_DATA_CONNECTION;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getMobileCountryCode() {
        return mobileCountryCode;
    }

    public void setMobileCountryCode(String mobileCountryCode) {
        this.mobileCountryCode = mobileCountryCode;
    }

    public String getMobileNetworkCode() {
        return mobileNetworkCode;
    }

    public void setMobileNetworkCode(String mobileNetworkCode) {
        this.mobileNetworkCode = mobileNetworkCode;
    }

    /**
     * Not-null value.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getVoipAvailable() {
        return voipAvailable;
    }

    public void setVoipAvailable(Boolean voipAvailable) {
        this.voipAvailable = voipAvailable;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public String toString() {
        return "MobileConnectionEventRequest{" +
                ", carrierName='" + carrierName + '\'' +
                ", mobileCountryCode='" + mobileCountryCode + '\'' +
                ", mobileNetworkCode='" + mobileNetworkCode + '\'' +
                ", created='" + created + '\'' +
                ", voipAvailable=" + voipAvailable +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
