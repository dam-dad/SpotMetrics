package spot.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spot.api.model.Item;
import spot.api.model.Me;
import spot.api.model.Token;
import spot.api.model.TopTracks;

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
	
	private String getBearer() {
		return "Bearer " + token.getAccessToken();
	}

	public String getUsername() throws IOException {		
		retrofit2.Response<Me> response = service.me(getBearer()).execute();
		return response.body().getDisplayName();
	}

	public List<Item> getTopTracks() throws IOException {
		retrofit2.Response<TopTracks> response = service.topTracks(getBearer(), "long_term", 1).execute();
		return response.body().getItems();
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
			spot.api.model.Error error = gson.fromJson(response.body().string(), spot.api.model.Error.class);
			throw new Exception(error.getErrorDescription());
		}
		
		return this.token;
		
	}

}
