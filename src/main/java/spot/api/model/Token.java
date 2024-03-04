package spot.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * access_token string An access token that can be provided in subsequent calls,
 * for example to Spotify Web API services. token_type string How the access
 * token may be used: always "Bearer". scope string A space-separated list of
 * scopes which have been granted for this access_token expires_in int The time
 * period (in seconds) for which the access token is valid. refresh_token string
 * See refreshing tokens.
 */
public class Token {

	@SerializedName("access_token")
	private String accessToken;

	@SerializedName("token_type")
	private String tokenType;

	@SerializedName("scope")
	private String scope;

	@SerializedName("expires_in")
	private Integer expiresIn;

	@SerializedName("refresh_token")
	private String refreshToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "Token [accessToken=" + accessToken + ", tokenType=" + tokenType + ", scope=" + scope + ", expiresIn="
				+ expiresIn + ", refreshToken=" + refreshToken + "]";
	}

}
