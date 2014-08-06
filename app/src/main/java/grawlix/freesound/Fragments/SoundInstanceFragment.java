package grawlix.freesound.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Sound;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 8/6/14.
 */
public class SoundInstanceFragment extends Fragment {
    public static final String SOUND_ID_KEY = "soundId";
    TextView textId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sound_instance_fragment_layout, container, false);

        textId = (TextView) view.findViewById(R.id.sound_instance_name);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("soundId", String.valueOf(getArguments().getInt(SOUND_ID_KEY)));
        loadSoundInstance(getArguments().getInt(SOUND_ID_KEY));
    }

    public void loadSoundInstance(int soundId) {

        FreesoundClient.getFreesoundApiClient().getSound(soundId, new Callback<Sound>() {
            @Override
            public void success(Sound sound, Response response) {
                Log.d("loadSoundInstance", "sucess");
                consumeApiData(sound);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("loadSoundInstance", "failure");
                Log.d("loadSoundInstace", retrofitError.getMessage());
                consumeApiData(null);
            }
        });
    }

    private void consumeApiData(Sound sound) {
        if (sound != null) {
            Log.d("sound NAme", sound.getName());
            textId.setText(String.valueOf(sound.getName()));
        }
    }
}
