package grawlix.freesound;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import grawlix.freesound.Fragments.ResultsFragment;

/**
 * Created by luismierez on 9/12/14.
 */
public class SearchResultsActivity extends Activity implements ResultsFragment.ResultsCommunicator {

    private int soundId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        // Get Action Bar
        ActionBar actionBar = getActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create a resultsfragment
        ResultsFragment resultsFragment = new ResultsFragment();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle args = new Bundle();
            args.putString("SEARCH_QUERY", query);
            resultsFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.search_result_frame, resultsFragment).commit();
        }
    }

    @Override
    public void respond(int soundId) {
        this.soundId = soundId;
        /*
        Intent intent = new Intent(this, SoundInstanceActivity.class);
        intent.putExtra("SOUND_ID", soundId);
        startActivity(intent);
        */

    }

}
