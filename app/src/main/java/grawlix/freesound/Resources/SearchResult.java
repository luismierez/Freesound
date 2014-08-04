package grawlix.freesound.Resources;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import grawlix.freesound.FreesoundAPI.FreesoundClient;

/**
 * Created by luismierez on 8/3/14.
 */
public class SearchResult {
    private List<Sound> _results;
    private int _count;
    private String _next;
    private String _previous;

    private FreesoundClient client = FreesoundClient.getInstance();

    public SearchResult(JSONObject searchObject) {
        _results = new ArrayList<Sound>();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            _count = searchObject.getInt("count");
            _next = searchObject.getString("next");
            JSONArray next = searchObject.getJSONArray("results");
            for (int i = 0; i < next.length(); i++) {
                _results.add(client.getSound(next.getJSONObject(i).getString("id"), params));
            }
            _previous = searchObject.getString("previous");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return _count;
    }

    public String getNext() {
        return _next;
    }

    public String getPrevious() {
        return _previous;
    }

    public int getResultsSize() {
        return _results.size();
    }

    public Sound getSound(int index) {
        return _results.get(index);
    }
}
