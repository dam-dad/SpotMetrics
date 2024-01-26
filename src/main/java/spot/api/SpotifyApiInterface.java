package spot.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import spot.api.model.Me;
import spot.api.model.TopTracks;

public interface SpotifyApiInterface {
	
	@GET("me")
	public Call<Me> me(@Header("Authorization") String bearer);

	@GET("me/top/tracks")
	public Call<TopTracks> topTracks(@Header("Authorization") String bearer, @Query("time_range") String timeRange, @Query("limit") Integer limit);
	
}