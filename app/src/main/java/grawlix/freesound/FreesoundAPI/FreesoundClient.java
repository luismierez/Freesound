package grawlix.freesound.FreesoundAPI;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import grawlix.freesound.Resources.Sound;
import grawlix.freesound.Util.JSONParser;

/**
 * Created by luismierez on 8/1/14.
 * Singleton class to handle all of the Freesound Api stuff
 */
public class FreesoundClient {
    private String _client_secret = "";
    private String client_id = "";
    private String token = "";
    private String header = "";
    private JSONParser jsonParser = new JSONParser();

    private static FreesoundClient _instance = null;

    protected FreesoundClient() {
        // Exists only to defeat instantiation
    }
    public static FreesoundClient getInstance() {
        if (_instance == null) {
            _instance = new FreesoundClient();
        }
        return _instance;
    }

    public void setClientSecret(String client_secret) {
        _client_secret = client_secret;
    }
    /**
     * This need to be called inside an AsyncTask so it doesn't lock the UI
     * @param soundId
     * @return Sound object
     */
    public Sound getSound(String soundId, List<NameValuePair> params) {
        String sounduri = URIS.SOUND_INSTANCE.replaceAll("<sound_id>", soundId);
        sounduri = URIS.BASE + sounduri;
        JSONObject soundObject = jsonParser.makeHttpRequest(sounduri, "GET", params);
        return new Sound(soundObject);
    }

    public ArrayList<Sound> textSearch(String searchTerm) {
        String searchURI = URIS.BASE + URIS.SEARCH_TEXT;
        searchURI = searchURI + searchTerm;
        return null;
    }




    private class URIS {
        static final String HOST = "www.freesound.org";
        static final String BASE = "https://" + HOST + "/apiv2";
        // Search Resources
        static final String SEARCH_TEXT = "/search/text/";
        static final String SEARCH_CONTENT = "/search/content/";
        static final String SEARCH_COMBINED = "/search/combined/";
        // Sound Resources
        static final String SOUND_INSTANCE = "/sounds/<sound_id>/";
        static final String SOUND_SIMILAR = "/sounds/<sound_id>/similar/";
        static final String SOUND_ANALYSIS = "/sounds/<sound_id>/analysis/";
        static final String SOUND_COMMENTS = "/sounds/<sound_id>/comments/";

        //  Rest require oauth - will be implemented later
        static final String SOUND_DOWNLOAD = "/sounds/<sound_id>/download/";
        static final String SOUND_BOOKMARK = "/sounds/<sound_id>/bookmark/";


    }
}
