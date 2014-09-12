package grawlix.freesound.Fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Images;
import grawlix.freesound.Resources.Sound;
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
                                                               View.OnTouchListener,
                                                               MediaPlayer.OnCompletionListener,
                                                               MediaPlayer.OnBufferingUpdateListener {

    public static final String SOUND_ID_KEY = "soundId";
    TextView textId;

    ImageButton buttonPlayPause;
    SeekBar seekBarProgress;
    MediaPlayer mediaPlayer;
    int mediaFileLengthInMilliseconds;
    String soundUrl = "http://www.freesound.org/data/previews/20/20049_92661-hq.mp3";
    int playPositionInMilliseconds;

    private final Handler handler = new Handler();

    // CardsUI stuff
    Card card;
    CardView cardView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        card = new Card(getActivity());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sound_instance_fragment_layout, container, false);

        textId = (TextView) view.findViewById(R.id.sound_instance_name);

        buttonPlayPause = (ImageButton) view.findViewById(R.id.button_play_pause);
        buttonPlayPause.setOnClickListener(this);
        seekBarProgress = (SeekBar) view.findViewById(R.id.seekbar);
        seekBarProgress.setMax(99); // 0-99 -> 99 = 100%
        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        try {
            mediaPlayer.setDataSource(soundUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
        return view;
    }

    // Method which updates the SeekBar primary progress by current song playing position
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100));

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                @Override
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);

        }
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
                Log.d("loadSoundInstance", "success");
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
            Log.d("sound Name", sound.getName());
            Log.d("sound Created", sound.getCreated());
            Log.d("sound license", sound.getLicense());
            Images images = sound.getImages();
            if (images.getSpectralL()==null)
                Log.d("spectralL", "null");

            if (images.getSpectralM()==null)
                Log.d("spectralM", "null");
            if (images.getWaveformL()==null)
                Log.d("waveformL", "null");
            if (images.getWaveformM()==null)
                Log.d("waveformM", "null");


            textId.setText(sound.getName());
            CardHeader cardHeader = new CardHeader(getActivity());
            cardHeader.setTitle(sound.getName());
            CardThumbnail thumbnail = new CardThumbnail(getActivity());
            //Log.d("image url", sound.getImages().getWaveformM());
            thumbnail.setUrlResource(sound.getImages().getWaveformM());
            thumbnail.setErrorResource(R.drawable.ic_launcher);
            card.addCardThumbnail(thumbnail);
            card.addCardHeader(cardHeader);
            cardView = (CardView) getActivity().findViewById(R.id.cardView);
            cardView.setCard(card);
        }
    }

    /** Playing sound stuff **/
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

        seekBarProgress.setSecondaryProgress(i);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.button_play_pause) {


            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                buttonPlayPause.setImageResource(R.drawable.button_pause);
            } else {
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(R.drawable.button_play);
            }

            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        buttonPlayPause.setImageResource(R.drawable.button_play);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId()== R.id.seekbar) {
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar)view;
                playPositionInMilliseconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMilliseconds);
            }
        }
        return false;
    }
}
