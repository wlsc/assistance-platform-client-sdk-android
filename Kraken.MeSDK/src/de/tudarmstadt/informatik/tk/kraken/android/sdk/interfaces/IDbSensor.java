package de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces;

import java.io.Serializable;

public interface IDbSensor extends Serializable {

    public void setTimestamp(Long timestamp);

    public Long getTimestamp();

    public Long getId();

    public void setId(Long id);

}
