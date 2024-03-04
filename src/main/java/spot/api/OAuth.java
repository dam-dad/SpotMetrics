package spot.api;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Clase para manejar la autenticación OAuth con Spotify.
 */
public class OAuth {

    // Credenciales de Spotify para consumir la API
    private ResourceBundle CONFIG = ResourceBundle.getBundle("config");
    private OAuthCallback authCallback;
    private HttpServer server;

    /**
     * Crea una instancia de OAuth y configura el servidor web para el proceso de autorización.
     *
     * @throws IOException Si hay un problema al crear el servidor HTTP.
     */
    public OAuth() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/callback", this::handleRequest);
    }

    /**
     * Inicia el proceso de autenticación OAuth.
     *
     * @param callback El objeto que manejará la respuesta de la autorización.
     * @throws IOException      Si hay un problema al iniciar el servidor HTTP.
     * @throws URISyntaxException Si hay un problema con la sintaxis de la URI.
     */
    public void auth(OAuthCallback callback) throws IOException, URISyntaxException {
        this.authCallback = callback;
        server.start();
        Desktop.getDesktop().browse(getAuthorizationEndpoint());
    }

    /**
     * Obtiene la URL de autorización de Spotify.
     *
     * @return La URI de autorización.
     * @throws URISyntaxException Si hay un problema con la sintaxis de la URI.
     */
    private URI getAuthorizationEndpoint() throws URISyntaxException {
        String clientId = CONFIG.getString("spotify.client.id");
        String redirectUri = CONFIG.getString("spotify.client.redirectUri");
        String responseType = "code";
        String scope = "user-top-read%20user-read-recently-played%20user-library-read%20playlist-read-private";
        String spotifyAuthorizationUrl = "https://accounts.spotify.com/authorize" +
                "?client_id=" + clientId +
                "&response_type=" + responseType +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope;
        return new URI(spotifyAuthorizationUrl);
    }

    /**
     * Convierte una cadena de consulta en un mapa de parámetros de consulta.
     *
     * @param query La cadena de consulta.
     * @return Un mapa de parámetros de consulta.
     */
    private Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    /**
     * Maneja la petición de redirección del servidor HTTP.
     *
     * @param exchange La instancia de HttpExchange.
     */
    private void handleRequest(HttpExchange exchange) {

        System.out.println("recibida petición");

        // Extrae el código de autorización de la URL de redirección
        URI request = exchange.getRequestURI();
        Map<String, String> query = queryToMap(request.getQuery());
        String authorizationCode = query.get("code");

        System.out.println("enviando respuesta ...");

        // Envía una respuesta al navegador del usuario
        try (OutputStream os = exchange.getResponseBody()) {
            String response = Files.readString(Paths.get(getClass().getResource("/response.html").toURI()), StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, 0);
            os.write(response.getBytes());
            os.flush();
            os.close();
            System.out.println("respuesta enviada");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("procesando código: " + authorizationCode);
        authCallback.handle(authorizationCode);

        // Detén el servidor web después de manejar la respuesta
        System.out.println("deteniendo servidor");
        server.stop(0);
    }

}
