package grawlix.freesound;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import grawlix.freesound.Fragments.SoundInstanceFragment;

/**
 * Created by luismierez on 9/22/14.
 */
public class SoundInstanceActivity extends Activity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_instance);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int soundID = intent.getIntExtra("SOUND_ID", 1111);

        Log.d("Sound ID", String.valueOf(soundID));

        // Set the fragment in the activity
        SoundInstanceFragment soundInstanceFragment = new SoundInstanceFragment();
        Bundle args = new Bundle();
        soundInstanceFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_sound_instance, soundInstanceFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
