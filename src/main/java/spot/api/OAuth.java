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

public class OAuth {
	
	private ResourceBundle CONFIG = ResourceBundle.getBundle("config");
	private OAuthCallback authCallback;
	private HttpServer server;
	
	public OAuth() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/callback", this::handleRequest);
	}
	
	public void auth(OAuthCallback callback) throws IOException, URISyntaxException {
		this.authCallback = callback;
		server.start();
		Desktop.getDesktop().browse(getAuthorizationEndpoint());
	}

    private URI getAuthorizationEndpoint() throws URISyntaxException {
        String clientId = CONFIG.getString("spotify.client.id");
        String redirectUri = CONFIG.getString("spotify.client.redirectUri");
        String responseType = "code";
        String scope = "user-top-read";
        String spotifyAuthorizationUrl = "https://accounts.spotify.com/authorize" +
                "?client_id=" + clientId +
                "&response_type=" + responseType +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope;
        return new URI(spotifyAuthorizationUrl);
    }
    
    private Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

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
