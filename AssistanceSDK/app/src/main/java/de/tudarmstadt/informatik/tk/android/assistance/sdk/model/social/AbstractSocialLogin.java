package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.social;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONObject;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.handler.IServerCommunicationResponseHandler;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.SocialNetworkEnum;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.SocialAuthProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.ui.activities.accounts.AccountsAdapter;


public abstract class AbstractSocialLogin implements SocialLogin {

    protected static Activity m_ctxActivity;


    @Override
    public void setContext(Activity ctx) {
        m_ctxActivity = ctx;
    }

    @Override
    public boolean isLoggedIn() {
        return (SocialAuthProvider.getInstance(m_ctxActivity).getAuthentication(getSocialNetworkProvider()) != null);
    }

    @Override
    public void logout(final AccountsAdapter adapter) {
        SocialAuthProvider.getInstance(m_ctxActivity).removeAuthentication(getSocialNetworkProvider());
        m_ctxActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.refreshItem(getName());
            }
        });
    }

    @Override
    public String getName() {
        return getSocialNetworkProvider().getProviderName();
    }

    public void handleReceivedToken(String strAccessToken, String strRefreshToken, final Activity context) {
        AccountVO vo = new AccountVO();
        vo.setAccessToken(strAccessToken);
        vo.setRefreshToken(strRefreshToken);
        handleReceivedToken(vo, context);
    }

    public void handleReceivedToken(AccountVO vo, final Activity context) {

        vo.setProvider(getSocialNetworkProvider());
//		JSONObject jsonVo = KrakenService.getJacksonObjectMapper().convertValue(vo, JSONObject.class);

        final JSONObject jsonObject = new JSONObject();
        try {
//			jsonObject.put("type", MessageType.LOGIN);
//			jsonObject.put("account", jsonVo);
//			jsonObject.put("device", buildDeviceInfoJson());

            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    MyHandler handler = new MyHandler();
//                    ServerCommunication com = new ServerCommunication(context, handler);
//                    com.sendGcmRegistrationToken(jsonObject);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class MyHandler implements IServerCommunicationResponseHandler {
        @Override
        public void handleData(Bundle data) {

            if (data.containsKey("response")) {
                String strResponse = data.getString("response");

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    boolean bSucc = jsonResponse.getBoolean("succ");
                    if (bSucc) {
                        JSONObject jsonData = jsonResponse.getJSONObject("data");
//                        String strKroken = jsonData.getString(MessageType.KEY_KRAKEN_TOKEN);
//                        SdkAuthentication.getInstance(m_ctxActivity).setKroken(strKroken);

                        JSONObject jsonAccount = jsonData.getJSONObject("account");
                        String strProvider = jsonAccount.getString("provider");
                        SocialNetworkEnum provider = SocialNetworkEnum.valueOf(strProvider);

                        JSONObject jsonAccessToken = jsonAccount.getJSONObject("accessToken");
                        String strToken = jsonAccessToken.getString("token");

                        // get used AccessToken and save in Preferences
                        SocialAuthProvider.getInstance(m_ctxActivity).setAuthentication(provider, strToken);

                        m_ctxActivity.finish();

                        return;
                    } else {
                        JSONObject jsonError = jsonResponse.getJSONObject("error");
                        String strMsg = jsonError.getString("msg");
                        String strCause = jsonError.getString("cause");
                        throw new Exception(strCause + ": " + strMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // TODO login failed
        }

    }


}
