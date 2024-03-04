/**
 * Interfaz que define los endpoints de la API de Spotify utilizando anotaciones Retrofit.
 */
package spot.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import spot.api.model.me.Me;
import spot.api.model.recommendations.RecommendedTracks;
import spot.api.model.topartists.TopArtists;
import spot.api.model.toptracks.TopTracks;

public interface SpotifyApiInterface {

    /**
     * Obtiene información sobre el usuario actual.
     *
     * @param bearer El token de autorización.
     * @return Un objeto Call de Retrofit con el modelo Me.
     */
    @GET("me")
    public Call<Me> me(@Header("Authorization") String bearer);

    /**
     * Obtiene las mejores canciones del usuario.
     *
     * @param bearer    El token de autorización.
     * @param timeRange El rango de tiempo para las mejores canciones.
     * @param limit     El número máximo de canciones a devolver.
     * @return Un objeto Call de Retrofit con el modelo TopTracks.
     */
    @GET("me/top/tracks")
    public Call<TopTracks> topTracks(@Header("Authorization") String bearer, @Query("time_range") String timeRange, @Query("limit") Integer limit);

    /**
     * Obtiene los mejores artistas del usuario.
     *
     * @param bearer    El token de autorización.
     * @param timeRange El rango de tiempo para los mejores artistas.
     * @param limit     El número máximo de artistas a devolver.
     * @return Un objeto Call de Retrofit con el modelo TopArtists.
     */
    @GET("me/top/artists")
    public Call<TopArtists> topArtists(@Header("Authorization") String bearer, @Query("time_range") String timeRange, @Query("limit") Integer limit);

    /**
     * Obtiene recomendaciones de canciones basadas en canciones semilla.
     *
     * @param bearer    El token de autorización.
     * @param seedTrack Una lista separada por comas de IDs o URIs de canciones semilla.
     * @param limit     El número máximo de canciones recomendadas a devolver.
     * @return Un objeto Call de Retrofit con el modelo RecommendedTracks.
     */
    @GET("recommendations")
    public Call<RecommendedTracks> recommendations(@Header("Authorization") String bearer, @Query("seed_tracks") String seedTrack, @Query("limit") Integer limit);

}
