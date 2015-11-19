package de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces;

public interface IDbSensor {

    Long getId();

    void setId(Long id);

    /**
     * @param date in ISO 8601 format
     */
    void setCreated(String date);

    /**
     * @return string in ISO 8601 format
     */
    String getCreated();

}
