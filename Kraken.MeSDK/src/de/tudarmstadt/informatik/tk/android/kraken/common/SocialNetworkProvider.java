package de.tudarmstadt.informatik.tk.android.kraken.common;

/**
 * Social network provider
 * 
 * @author Roman B�rtl, Christian Meurisch
 * @version 1.0
 * @date 17.10.2013
 */
public enum SocialNetworkProvider {
	FACEBOOK, GOOGLE, TWITTER, LIVE, FOURSQUARE, LINKEDIN, XING;

	public String getProviderName() {
		switch (this) {
		case FACEBOOK:
			return "Facebook";
		case FOURSQUARE:
			return "Foursquare";
		case GOOGLE:
			return "Google";
		case LINKEDIN:
			return "LinkedIn";
		case LIVE:
			return "MS Live";
		case TWITTER:
			return "Twitter";
		case XING:
			return "Xing";
		}
		return "";
	}

}
