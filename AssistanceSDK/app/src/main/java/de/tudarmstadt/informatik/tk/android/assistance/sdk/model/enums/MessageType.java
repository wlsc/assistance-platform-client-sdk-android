package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums;

/**
 * Message types
 * 
 * @author Roman B�rtl, Christian Meurisch
 * @version 1.0
 * @date 11.10.2013
 */
public enum MessageType {
	LOGIN, ACCOUNT, PERSONAL_DATA, PERSONAL_DATA_QUERY, UNKNOWN_TYPE, 
	
	BUNDLE, GCM_REGISTRATION,
	
	TIME_SYNC,
	
	// DEBUG STUFF
	DEBUG_JSON_STRUCTURE, DEBUG_KROKEN;

	public static final String KEY_MESSAGE_TYPE = "type";
	public static final String KEY_KRAKEN_TOKEN	= "kroken";
	public static final String KEY_DEVICE_ID	= "deviceID";
	public static final String KEY_DATA 		= "data";
}
