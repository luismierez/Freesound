package grawlix.freesound.FreesoundAPI;

import com.google.gson.GsonBuilder;

import grawlix.freesound.Resources.SearchText;
import grawlix.freesound.Resources.Sound;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luismierez on 8/5/14.
 */
public class FreesoundClient {
    private static Freesound mFreesoundService;
    private static String api_key = "97cd22ae047813db794abfb26de7a43273e0d5f6";

    public static Freesound getFreesoundApiClient() {
        if (mFreesoundService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://www.freesound.org/apiv2")
                    .setLogLevel(RestAdapter.LogLevel.FULL) // Log everything
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade requestFacade) {
                            requestFacade.addHeader("Authorization", "Token " + api_key);
                        }
                    })
                    .build();

            mFreesoundService = restAdapter.create(Freesound.class);
        }

        return mFreesoundService;
    }

    public interface Freesound {

        @GET("/sounds/{sound_id}")
        void getSound(
            @Path("sound_id") int sound_id,
            Callback<Sound> callback
        );

        @GET("/search/text/?fields=id,name,tags,username,previews,images,geotag")
        void searchText(
                @Query("query") String query,
                @Query("page") int page,
                Callback<SearchText> callback
        );

        @GET("/search/text/?fields=id,name,tags,username,previews,images,geotag")
        void searchText(
                @Query("query") String query,
                @Query("page") int page,
                @Query("sort") String sort,
                Callback<SearchText> callback
        );

        @GET("/search/text/?fields=id,name,tags,username,previews,images,geotag")
        void searchText(
                @Query("query") String query,
                Callback<SearchText> callback
        );

        @GET("/search/text/?fields=id,name,tags,username,previews,images,geotag")
        void geoSearch(
                @Query("filter")String geoQuery,
                Callback<SearchText> callback
        );
    }
}
