package de.tudarmstadt.informatik.tk.android.kraken.api.entities;

import java.io.Serializable;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.common.MessageType;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;

/**
 * Message
 *
 * @author Karsten Planz
 */
public class ApiMessage implements Serializable {
    public MessageType type;
    public String kroken;
    public String deviceID;
    public DataWrapper payload;
    public List<ApiMessage> data;

    public static class DataWrapper {
        public String databaseClassName;
        public String className;
        public List<? extends IDbSensor> objs;
    }
}
