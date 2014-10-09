package grawlix.freesound.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by luismierez on 10/8/14.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
                                                     MediaPlayer.OnErrorListener,
                                                     MediaPlayer.OnCompletionListener{

    // media player
    private MediaPlayer player;
    // current position
    private int songPosition;
    // sound url
    private String songUrl;

    private final IBinder musicBind = new MusicBinder();

    public void onCreate() {
        // create the service
        super.onCreate();
        // initialize position
        songPosition=0;
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

    public void setSongUrl(String url) {
        songUrl = url;
    }

    public void playSong() {
        // play a song
        player.reset();
        try {
            player.setDataSource(songUrl);
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error setting data source ", e);
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
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // start playback
        mediaPlayer.start();
    }
}
