package de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.common.MessageType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;

/**
 * Message
 *
 * @author Karsten Planz
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiMessage implements Serializable {
    public MessageType type;
    public String kroken;
    public String deviceID;
    public DataWrapper payload;
    public List<ApiMessage> data;

    public static class DataWrapper {
        @JsonIgnore
        public String databaseClassName;
        @JsonProperty("class")
        public String className;
        public List<? extends IDbSensor> objs;
    }
}
