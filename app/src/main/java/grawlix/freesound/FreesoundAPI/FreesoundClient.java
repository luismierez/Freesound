package grawlix.freesound.FreesoundAPI;

import grawlix.freesound.Resources.SearchText;
import grawlix.freesound.Resources.Sound;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luismierez on 8/5/14.
 */
public class FreesoundClient {
    private static Freesound mFreesoundService;

    public static Freesound getFreesoundApiClient() {
        if (mFreesoundService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://www.freesound.org/apiv2")
                    .build();

            mFreesoundService = restAdapter.create(Freesound.class);
        }

        return mFreesoundService;
    }

    public interface Freesound {
        @GET("/sounds/{sound_id}")
        Sound getSound(
            @Path("sound_id") String sound_id
        );

        @GET("/search/text/")
        void searchText(@Query("query") String query, @Query("token") String token, Callback<SearchText> callback);
    }
}
