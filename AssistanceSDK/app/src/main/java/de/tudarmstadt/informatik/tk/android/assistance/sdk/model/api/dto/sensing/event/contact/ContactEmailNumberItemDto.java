package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 11.12.2015
 */
public class ContactEmailNumberItemDto {

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("value")
    @Expose
    private String value;

    public ContactEmailNumberItemDto(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ContactEmailNumberItemDto{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}