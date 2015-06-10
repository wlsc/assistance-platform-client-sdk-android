package de.tudarmstadt.informatik.tk.kraken.android.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.IServerCommunicationResponseHandler;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.ServerCommunication;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.KrakenUtils;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.common.MessageType;

public class GcmManager {

	private static final String PROPERTY_REG_ID = "GcmRegId";
	private static final String APP_VERSION = "AppVersion";
	protected static final String SENDER_ID = "930932669428";
	private static GcmManager m_instance;
	private static Context m_context;
	private GoogleCloudMessaging m_gcm;
	private String m_strRegId;

	private GcmManager() {

	}

	public static GcmManager getInstance(Context context) {
		m_context = context;
		if (m_instance == null)
			m_instance = new GcmManager();
		return m_instance;
	}

	public static GcmManager getInstance() {
		return m_instance;
	}

	public void registerAtCloud() {
		m_gcm = GoogleCloudMessaging.getInstance(m_context);
		m_strRegId = getRegistrationId(m_context);

		if (m_strRegId.isEmpty()) {
			registerInBackground();
		}

	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}

		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences() {
		return m_context.getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {

		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String msg = "";
				try {
					if (m_gcm == null) {
						m_gcm = GoogleCloudMessaging.getInstance(m_context);
					}
					m_strRegId = m_gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + m_strRegId;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					try {
						sendRegistrationIdToBackend();
					} catch (JSONException e) {
						e.printStackTrace();
					}

					// Persist the regID - no need to register again.
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

		}.execute();

	}

	private void sendRegistrationIdToBackend() throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(MessageType.KEY_MESSAGE_TYPE, MessageType.GCM_REGISTRATION);
		JSONObject jsonGcm = new JSONObject();
		TelephonyManager systemService = (TelephonyManager) m_context.getSystemService(Context.TELEPHONY_SERVICE);
		String strDeviceId = systemService.getDeviceId();
        if(strDeviceId == null) {
            strDeviceId = KrakenUtils.getDeviceId(m_context);
        }
		jsonGcm.put("deviceID", hash(strDeviceId));
		jsonGcm.put("registrationToken", m_strRegId);
		jsonObject.put("gcm", jsonGcm);

		ServerCommunication serverCommunication = new ServerCommunication(m_context, new GcmRegIdSentHandler());
		serverCommunication.postRequest(jsonObject);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 */
	private void storeRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		int appVersion = getAppVersion(m_context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, m_strRegId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

	@SuppressLint("DefaultLocale")
	private String hash(String value) {
		String hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = value.getBytes("UTF-8");
			digest.update(bytes, 0, bytes.length);
			bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02X", b));
			}
			hash = sb.toString().toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

	private static class GcmRegIdSentHandler implements IServerCommunicationResponseHandler {
		@Override
		public void handleData(Bundle data) {
			if (data.containsKey("error") && data.getBoolean("error") == false) {
				getInstance().storeRegistrationId();
			}
		}
	}
}
