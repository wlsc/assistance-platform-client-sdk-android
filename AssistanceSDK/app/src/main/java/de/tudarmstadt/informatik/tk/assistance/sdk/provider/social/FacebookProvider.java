package de.tudarmstadt.informatik.tk.assistance.sdk.provider.social;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import de.tudarmstadt.informatik.tk.assistance.sdk.handler.OnFacebookGraphResponse;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.12.2015
 */
public class FacebookProvider {

    private static final String TAG = FacebookProvider.class.getSimpleName();

    private static FacebookProvider INSTANCE;

    private Context context;

    private FacebookProvider(Context context) {
        this.context = context;
    }

    public static FacebookProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new FacebookProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Requests user graph data
     * Use permission parameters e.g. "id, first_name, last_name, email, gender"
     *
     * @param accessToken
     * @param permissionParams
     * @param onFacebookGraphResponse
     */
    public void requestGraphData(final AccessToken accessToken,
                                 String permissionParams,
                                 final OnFacebookGraphResponse onFacebookGraphResponse) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                (object, response) -> {

                    if (object == null) {
                        Log.d(TAG, "Response is null");
                        return;
                    }

                    Log.d(TAG, "Object received: " + object.toString());

                    onFacebookGraphResponse.onCompleted(object, response);
                }
        );

        Bundle parameters = new Bundle();

        parameters.putString("fields", permissionParams);

        request.setParameters(parameters);
        request.executeAsync();
    }
}