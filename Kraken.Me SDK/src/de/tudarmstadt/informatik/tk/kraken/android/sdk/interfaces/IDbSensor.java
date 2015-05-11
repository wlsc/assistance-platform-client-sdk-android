package de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface IDbSensor extends Serializable {

	public void setTimestamp(Long timestamp);

	public Long getTimestamp();

    @JsonIgnore
	public Long getId();

	public void setId(Long id);

}
