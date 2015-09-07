package de.tudarmstadt.informatik.tk.android.kraken.interfaces;

public interface IDbUpdatableSensor extends IDbSensor {

	public void setTimestamp(Long timestamp);

	public Boolean getIsNew();

	public Boolean getIsUpdated();

	public Boolean getIsDeleted();

	public void setIsNew(Boolean b);

	public void setIsUpdated(Boolean b);

	public void setIsDeleted(Boolean b);

}
