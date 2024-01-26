package spot.api;

public interface OAuthCallback {
	
	public void handle(String authCode);

}
