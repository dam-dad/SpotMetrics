package spot.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spot.api.model.Token;
import spot.api.model.me.Me;
import spot.api.model.recommendations.RecommendedTracks;
import spot.api.model.topartists.TopArtists;
import spot.api.model.toptracks.Item;
import spot.api.model.toptracks.TopTracks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class SpotifyApi {

	private ResourceBundle CONFIG = ResourceBundle.getBundle("config");

	private static final String API_URL = "https://api.spotify.com/v1/";
	private static final String ACCOUNTS_URL = "https://accounts.spotify.com/api/token";

	private OkHttpClient client;
	private Gson gson;
	private Token token;
	private SpotifyApiInterface service;

	public SpotifyApi() {

		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(Level.BODY);

		client = new OkHttpClient.Builder()
				.addInterceptor(logging)
				.connectionPool(new ConnectionPool(1, 5, TimeUnit.SECONDS))
				.build();

		gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(API_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.client(client)
				.build();

		service = retrofit.create(SpotifyApiInterface.class);
	}

	public void setCONFIG(ResourceBundle CONFIG) {
		this.CONFIG = CONFIG;
	}

	public void setClient(OkHttpClient client) {
		this.client = client;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public void setService(SpotifyApiInterface service) {
		this.service = service;
	}

	private String getBearer() {
		return "Bearer " + token.getAccessToken();
	}

	public String getUsername() throws IOException {
		retrofit2.Response<Me> response = service.me(getBearer()).execute();
		if (!response.isSuccessful()) {
			throw new IOException(response.raw().toString());			
		}
		System.out.println(response.raw());
		return response.body().getDisplayName();
	}

	public String getUserImage() throws IOException {
		retrofit2.Response<Me> response = service.me(getBearer()).execute();
		if (!response.isSuccessful()) {
			throw new IOException(response.raw().toString());
		}
		System.out.println(response.raw());
		return response.body().getImages().get(0).getUrl();
	}

	public String getUserUrl() throws IOException {
		retrofit2.Response<Me> response = service.me(getBearer()).execute();
		if (!response.isSuccessful()) {
			throw new IOException(response.raw().toString());
		}
		System.out.println(response.raw());
		return response.body().getExternalUrls().getSpotify();
	}

	public List<Item> getTopTracks() throws IOException {
		retrofit2.Response<TopTracks> response = service.topTracks(getBearer(), "long_term", 5).execute();
		return response.body().getItems();
	}

	public List<spot.api.model.topartists.Item> getTopArtists() throws IOException {
		retrofit2.Response<TopArtists> response = service.topArtists(getBearer(), "long_term", 5).execute();
		System.out.println("Bearer de getTopArtits " + getBearer());
		return response.body().getItems();
	}

	public List<spot.api.model.recommendations.Track> getRecommendations(List<String> idsCanciones) throws IOException {
		String seedTracks = StringUtils.join(idsCanciones, ',');	
		retrofit2.Response<RecommendedTracks> response = service.recommendations(getBearer(), seedTracks, 5).execute();
		
		return response.body().getTracks();
	}

	public Token getToken() {
		return token;
	}

	// Implementación del método para intercambiar el código de autorización por el
	// token de acceso
	public Token requestAccessToken(String authorizationCode) throws Exception {

		String clientId = CONFIG.getString("spotify.client.id");
		String clientSecret = CONFIG.getString("spotify.client.secret");
		String redirectUri = CONFIG.getString("spotify.client.redirectUri");

		RequestBody body = new FormBody.Builder()
				.add("grant_type", "authorization_code")
				.add("code", authorizationCode)
				.add("redirect_uri", redirectUri)
				.build();

		// Construye la cadena "client_id:client_secret" y la codifica en Base64
		String credentials = clientId + ":" + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

		Request request = new Request.Builder()
				.url(ACCOUNTS_URL)
				.addHeader("Authorization", "Basic " + encodedCredentials)
				.post(body)
				.build();

		Response response = client.newCall(request).execute();

		if (response.isSuccessful()) {
			this.token = gson.fromJson(response.body().string(), Token.class);
		} else {
			spot.api.model.error.Error error = gson.fromJson(response.body().string(), spot.api.model.error.Error.class);
			throw new Exception(error.getErrorDescription());
		}

		return this.token;

	}

}
