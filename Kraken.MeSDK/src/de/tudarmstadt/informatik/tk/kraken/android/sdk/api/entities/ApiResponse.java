package de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities;

import org.json.JSONObject;

import java.util.List;

/**
 * ApiResponse
 *
 * @author Karsten Planz
 */
public class ApiResponse {
    public boolean succ;
    public long t_server;
    public ApiError error;
    public long querytime_ms;

    public static class ApiError {
        public String cause;
        public String code;
        public String msg;
    }

    public static class SingleApiResponse extends ApiResponse {
        public JSONObject data;
    }

    public static class BundleApiResponse extends ApiResponse {
        public BundleApiResponseData data;
    }

    public static class ArrayApiResponse<T> extends ApiResponse {
        public List<T> data;
    }

    public static class BundleApiResponseData extends ApiResponse {
        public List<SingleApiResponse> data;
        public String type;
    }
}
