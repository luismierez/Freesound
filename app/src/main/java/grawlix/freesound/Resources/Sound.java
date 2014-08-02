package grawlix.freesound.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luismierez on 8/1/14.
 */
public class Sound {
    private int _id;
    private String _url;
    private String _name;
    private List<String> _tags = new ArrayList<String>();
    private String _description;
    //private String _geotag;
    private String _created;
    private String _license;
    private String _type;
    private int _channels;
    private int _filesize;
    private int _bitrate;
    private int _bitdepth;
    private int _duration;
    private int _samplerate;
    private String _username;
    private String _pack;
    private List<String> _previews = new ArrayList<String>();
    private List<String> _images = new ArrayList<String>();

    public Sound(JSONObject soundObject) {

        try {
            _id = soundObject.getInt("id");                         // Get id of sound instance
            _url = soundObject.getString("url");                    // Get url of sound instance
            _name = soundObject.getString("name");                  // Get name of sound instance
            JSONArray tags = soundObject.getJSONArray("tags");      // Get tags of sound instance
            for (int i = 0; i < tags.length(); i++) {
                _tags.add(tags.getString(i));
            }
            _description = soundObject.getString("description");
            _created = soundObject.getString("created");
            _license = soundObject.getString("license");
            _type = soundObject.getString("type");
            _filesize = soundObject.getInt("filesize");
            _bitrate = soundObject.getInt("bitrate");
            _bitdepth = soundObject.getInt("bitdepth");
            _duration = soundObject.getInt("duration");
            _samplerate = soundObject.getInt("samplerate");
            _username = soundObject.getString("username");
            _images.add(soundObject.getJSONObject("images").getString("waveform_l"));
            _images.add(soundObject.getJSONObject("images").getString("waveform_m"));
            _images.add(soundObject.getJSONObject("images").getString("spectral_l"));
            _images.add(soundObject.getJSONObject("images").getString("spectral_m"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return _id;
    }

    public String getUrl() {
        return _url;
    }

    public String getName() {
        return _name;
    }

    public List<String> getTags() {
        return _tags;
    }

    public String getDescription() {
        return _description;
    }

    public String getCreate() {
        return _created;
    }
}
