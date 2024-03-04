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

/**
 * Clase para interactuar con la API de Spotify.
 */
public class SpotifyApi {

    // Implementación del método para obtener el token de acceso
    private ResourceBundle CONFIG = ResourceBundle.getBundle("config");

    // Constantes de la API de Spotify
    private static final String API_URL = "https://api.spotify.com/v1/";
    private static final String ACCOUNTS_URL = "https://accounts.spotify.com/api/token";

    // Implementación de los atributos
    private OkHttpClient client;
    private Gson gson;
    private Token token;
    private SpotifyApiInterface service;

    // Constructor
    public SpotifyApi() {

        // Configuración del cliente OkHttpClient
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectionPool(new ConnectionPool(1, 5, TimeUnit.SECONDS))
                .build();

        // Configuración del objeto Gson para la serialización y deserialización
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // Configuración del objeto Retrofit para la interacción con la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(SpotifyApiInterface.class);
    }

    // ----    Getters y setters para la interacción con la API de Spotify    ----

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

    /**
     * Obtiene el nombre de usuario de Spotify.
     *
     * @return El nombre de usuario.
     * @throws IOException Si ocurre un error durante la comunicación con la API.
     */
    public String getUsername() throws IOException {
        retrofit2.Response<Me> response = service.me(getBearer()).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.raw().toString());
        }
        System.out.println(response.raw());
        return response.body().getDisplayName();
    }

    /**
     * Obtiene la URL de la imagen de perfil del usuario de Spotify.
     *
     * @return La URL de la imagen de perfil.
     * @throws IOException Si ocurre un error durante la comunicación con la API.
     */
    public String getUserImage() throws IOException {
        retrofit2.Response<Me> response = service.me(getBearer()).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.raw().toString());
        }
        System.out.println(response.raw());
        return response.body().getImages().get(0).getUrl();
    }

    /**
     * Obtiene la URL del perfil de usuario de Spotify.
     *
     * @return La URL del perfil de usuario.
     * @throws IOException Si ocurre un error durante la comunicación con la API.
     */
    public String getUserUrl() throws IOException {
        retrofit2.Response<Me> response = service.me(getBearer()).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.raw().toString());
        }
        System.out.println(response.raw());
        return response.body().getExternalUrls().getSpotify();
    }

    /**
     * Obtiene las canciones más escuchadas por el usuario.
     *
     * @return La lista de canciones más escuchadas.
     * @throws IOException Si ocurre un error durante la comunicación con la API.
     */
    public List<Item> getTopTracks() throws IOException {
        retrofit2.Response<TopTracks> response = service.topTracks(getBearer(), "long_term", 5).execute();
        return response.body().getItems();
    }

    /**
     * Obtiene los artistas más escuchados por el usuario.
     *
     * @return La lista de artistas más escuchados.
     * @throws IOException Si ocurre un error durante la comunicación con la API.
     */
    public List<spot.api.model.topartists.Item> getTopArtists() throws IOException {
        retrofit2.Response<TopArtists> response = service.topArtists(getBearer(), "long_term", 5).execute();
        System.out.println("Bearer de getTopArtits " + getBearer());
        return response.body().getItems();
    }

    /**
     * Obtiene recomendaciones de canciones basadas en una lista de identificadores de canciones.
     *
     * @param idsCanciones La lista de identificadores de canciones.
     * @return La lista de recomendaciones de canciones.
     * @throws IOException Si ocurre un error durante la comunicación con la API.
     */
    public List<spot.api.model.recommendations.Track> getRecommendations(List<String> idsCanciones) throws IOException {
        String seedTracks = StringUtils.join(idsCanciones, ',');
        retrofit2.Response<RecommendedTracks> response = service.recommendations(getBearer(), seedTracks, 5).execute();

        return response.body().getTracks();
    }

    /**
     * Obtiene el token de acceso de Spotify.
     *
     * @return El token de acceso.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Intercambia el código de autorización por el token de acceso.
     *
     * @param authorizationCode El código de autorización.
     * @return El token de acceso.
     * @throws Exception Si ocurre un error durante la comunicación con la API.
     */
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
