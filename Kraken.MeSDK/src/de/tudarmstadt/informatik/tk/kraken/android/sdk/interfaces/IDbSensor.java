package de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces;

import java.io.Serializable;

public interface IDbSensor extends Serializable {

    long getId();

    void setId(long id);

    /**
     * @param date in ISO 8601 format
     */
    void setCreated(String date);

    /**
     * @return string in ISO 8601 format
     */
    String getCreated();

}
