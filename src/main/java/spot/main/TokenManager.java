package spot.main;

import java.io.*;
import java.util.Properties;

public class TokenManager {
    private static final String TOKEN_FILE = "token.properties";
    private static final String TOKEN_PROPERTY = "access_token";

    public static void saveToken(String accessToken) {
        try (OutputStream output = new FileOutputStream(TOKEN_FILE)) {
            Properties properties = new Properties();
            properties.setProperty(TOKEN_PROPERTY, accessToken);
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadToken() {
        try (InputStream input = new FileInputStream(TOKEN_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(TOKEN_PROPERTY);
        } catch (IOException e) {
            return null; // No se pudo cargar el token
        }
    }
}
