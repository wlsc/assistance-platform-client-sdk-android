package de.tudarmstadt.informatik.tk.kraken.android.sdk.common.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.common.SocialNetworkProvider;


/**
 * LoginVO (used by jackson)
 * @author Roman Bärtl, Christian Meurisch
 * @version 1.0
 * @date 01.12.2013
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountVO implements Serializable {

	public static final String KEY_AUTHENTICATION = "authentication";
	
	private static final long serialVersionUID = 1227902155752371848L;

	private SocialNetworkProvider provider;
	
	private PermissionLevel permissionLevel;
	
	private String accessToken;
	private String accessTokenSecret;
	
	private String refreshToken;
	private String authCode;
	
	private String requestToken;
	private String requestTokenSecret;
	private String requestTokenVerifier;
	
	
	/**
	 * Constructor
	 */
	public AccountVO() {}


	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}


	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}


	public String getRefreshToken() {
		return refreshToken;
	}


	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}


	public String getAuthCode() {
		return authCode;
	}


	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}


	public String getRequestToken() {
		return requestToken;
	}


	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}


	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}


	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}


	public String getRequestTokenVerifier() {
		return requestTokenVerifier;
	}


	public void setRequestTokenVerifier(String requestTokenVerifier) {
		this.requestTokenVerifier = requestTokenVerifier;
	}


	public SocialNetworkProvider getProvider() {
		return provider;
	}


	public void setProvider(SocialNetworkProvider provider) {
		this.provider = provider;
	}


	public PermissionLevel getPermissionLevel() {
		return permissionLevel;
	}


	public void setPermissionLevel(PermissionLevel permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	
}
