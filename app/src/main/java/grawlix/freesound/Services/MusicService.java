package grawlix.freesound.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

import grawlix.freesound.Fragments.ResultsFragment;
import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Sound;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 10/8/14.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
                                                     MediaPlayer.OnErrorListener,
                                                     MediaPlayer.OnCompletionListener{

    // media player
    private MediaPlayer player;

    // sound url
    private String soundUrl;

    // sound id
    private int soundId;

    private final IBinder musicBind = new MusicBinder();

    private String songTitle = "";
    private static final int NOTIFY_ID=1;

    public void onCreate() {
        // create the service
        super.onCreate();

        // create player
        player = new MediaPlayer();

        initMusicPlayer();
    }

    public void initMusicPlayer() {
        Log.d("MusicService", "Initialize music player");
        // set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setSongId(int id) {
        soundId = id;

    }

    public void playSong() {
        // play a song
        player.reset();
        FreesoundClient.getFreesoundApiClient().getSound(soundId, new Callback<Sound>() {
            @Override
            public void success(Sound sound, Response response) {
                soundUrl = sound.getPreviews().getPreviewHqMp3();
                try {
                    player.setDataSource(soundUrl);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.prepareAsync();
                songTitle = sound.getName();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    // Binder Class
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (player.getCurrentPosition()>0) {
            mediaPlayer.reset();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // start playback
        mediaPlayer.start();

        Intent notIntent = new Intent(this, ResultsFragment.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.android_music_player_play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public int getDuration() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int position) {
        player.seekTo(position);
    }

    public void start() {
        player.start();
    }
}
