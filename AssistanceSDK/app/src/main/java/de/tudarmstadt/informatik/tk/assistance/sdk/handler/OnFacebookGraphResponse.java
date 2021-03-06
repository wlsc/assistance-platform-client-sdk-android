package de.tudarmstadt.informatik.tk.assistance.sdk.handler;

import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.12.2015
 */
@FunctionalInterface
public interface OnFacebookGraphResponse {

    void onCompleted(JSONObject object, GraphResponse response);
}
