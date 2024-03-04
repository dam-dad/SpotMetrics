/**
 * Interfaz para manejar el código de autorización OAuth.
 */
package spot.api;

public interface OAuthCallback {

	/**
	 * Maneja el código de autorización OAuth.
	 *
	 * @param authCode El código de autorización recibido.
	 */
	public void handle(String authCode);

}
