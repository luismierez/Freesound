package grawlix.freesound.Fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import grawlix.freesound.Controllers.MusicController;
import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Images;
import grawlix.freesound.Resources.Previews;
import grawlix.freesound.Resources.Sound;
import grawlix.freesound.Services.MusicService;
import grawlix.freesound.Services.MusicService.MusicBinder;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 8/6/14.
 */
public class SoundInstanceFragment extends Fragment implements View.OnClickListener,
                                                               MediaController.MediaPlayerControl {

    public static final String SOUND_ID_KEY = "soundId";
    TextView textId;

    ImageButton buttonPlayPause;
    SeekBar seekBarProgress;
    MediaPlayer mediaPlayer;

    String soundUrl = "";

    // SERVICE STUFF
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound=false;

    // Controller
    private MusicController controller;

    // CardsUI stuff
    Card card;
    CardView cardView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        card = new Card(getActivity());
        setController();

    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicBinder binder = (MusicBinder) service;
            // get service
            musicService = binder.getService();
            // pass song url
            //musicService.setSongUrl(soundUrl);

            if (musicService==null)
                Log.d("musicService in ServiceConnection", "null");
            else
                Log.d("musicService in ServiceConnection", "not null");

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        loadSoundInstance(getArguments().getInt(SOUND_ID_KEY));
        Log.d("onStart", "here");
        if(playIntent==null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
            if (musicService==null)
                Log.d("musicService in onStart", "null");
            else
                Log.d("musicService in onStart", "not null");

            Log.d("soundUrl", soundUrl);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sound_instance_fragment_layout, container, false);

        textId = (TextView) view.findViewById(R.id.sound_instance_name);
        cardView = (CardView) view.findViewById(R.id.cardView);
        buttonPlayPause = (ImageButton) view.findViewById(R.id.button_play_pause);
        buttonPlayPause.setOnClickListener(this);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("soundId", String.valueOf(getArguments().getInt(SOUND_ID_KEY)));

    }

    @Override
    public void onDestroy() {
        getActivity().stopService(playIntent);
        musicService=null;
        super.onDestroy();
    }

    public void loadSoundInstance(int soundId) {

        FreesoundClient.getFreesoundApiClient().getSound(soundId, new Callback<Sound>() {
            @Override
            public void success(Sound sound, Response response) {
                Log.d("loadSoundInstance", "success");
                Log.d("loadSoundInstance", response.getReason());
                consumeApiData(sound);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("loadSoundInstance", "failure");
                Log.d("loadSoundInstace", retrofitError.getMessage());
                Log.d("soundUrl", soundUrl);
                consumeApiData(null);
            }
        });
    }

    private void consumeApiData(Sound sound) {
        if (sound != null) {

            Images images = sound.getImages();
            Previews previews = sound.getPreviews();

            List<String> tags = sound.getTags();

            textId.setText(sound.getName());
            // Create Header
            CardHeader cardHeader = new CardHeader(getActivity());
            // Set Header Title
            cardHeader.setTitle(sound.getName());
            // Add Header to card
            card.addCardHeader(cardHeader);
            // Create thumbnail
            CardThumbnail thumbnail = new CardThumbnail(getActivity());
            // Set the url for the thumbnail
            thumbnail.setUrlResource(images.getWaveformL());
            //thumbnail.setDrawableResource(R.drawable.button_pause);
            thumbnail.setErrorResource(R.drawable.ic_launcher);
            card.addCardThumbnail(thumbnail);
            cardView.setCard(card);

            soundUrl = previews.getPreviewHqMp3();
            Log.d("soundUrl", soundUrl);
        }
    }

    /** Playing sound stuff **/

    private void setController() {
        // set the controller up
        controller = new MusicController(getActivity());
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(getActivity().findViewById(R.id.fragment_sound_instance));
        controller.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.button_play_pause) {
            Log.d("soundUrl", soundUrl);
            //musicService.setSongUrl(soundUrl);
            musicService.playSong();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int i) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
