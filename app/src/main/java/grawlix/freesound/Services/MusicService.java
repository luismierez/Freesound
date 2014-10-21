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
import java.util.Random;

import grawlix.freesound.Fragments.ResultsFragment;

import grawlix.freesound.R;

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

    private boolean shuffle = false;
    private Random rand;

    public void onCreate() {
        // create the service
        super.onCreate();

        rand = new Random();
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

    public void setSongUrl(String soundUrl) {
        this.soundUrl = soundUrl;

    }

    public void playSong() {
        // play a song
        player.reset();
        try {
            Log.d("playSong() soundUrl", soundUrl);
            player.setDataSource(soundUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
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
        Log.d("onUnbind", "Called");
        player.stop();

        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
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
        Log.d("onPrepared", "Arrived");
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
        Log.d("getDuration", String.valueOf(player.getDuration()));
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

    public void go() {
        player.start();
    }

    public void setShuffle() {
        if (shuffle) shuffle=false;
        else shuffle=true;
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }
}

