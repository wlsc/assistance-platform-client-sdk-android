package de.tudarmstadt.informatik.tk.android.kraken.interfaces;

import java.io.Serializable;

public interface IDbSensor extends Serializable {

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
