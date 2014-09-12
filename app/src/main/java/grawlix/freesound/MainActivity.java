package grawlix.freesound;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import grawlix.freesound.Fragments.ResultsFragment;
import grawlix.freesound.Fragments.SoundInstanceFragment;
import grawlix.freesound.Resources.SearchText;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements ResultsFragment.ResultsCommunicator {

    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private int mSelectedFragment;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private String[] test = {"Home", "Random Sound of the Day", "Most Downloaded Sounds"};

    //------------Fragments----------------//
    FragmentManager manager;
    //ResultsFragment resultsFragment;
    //------------Fragments----------------//

    /*
        This method allows us to listen to ResultsFragment
     */
    @Override
    public void respond(int soundId) {

        Log.d("Main Activity respond", String.valueOf(soundId));
        Bundle args = new Bundle();
        args.putInt(SoundInstanceFragment.SOUND_ID_KEY, soundId);
        SoundInstanceFragment soundInstance = new SoundInstanceFragment();
        soundInstance.setArguments(args);

        manager.beginTransaction().replace(R.id.fragment_main, soundInstance).commit();

    }

    private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT, mSelectedFragment);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        // Set the list fragment initially
        manager.beginTransaction().replace(R.id.fragment_main, new ResultsFragment())
                .addToBackStack(null)
                .commit();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.drawer);

        // Set the adapter for drawer list
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_layout, test));

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
        mDrawer.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPostCreate(Bundle savendInstanceState) {
        super.onPostCreate(savendInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

        public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout) {
            super (activity,
                    drawerLayout,
                    R.drawable.ic_navigation_drawer,
                    R.string.drawer_open,
                    R.string.drawer_close);
        }

        @Override
        public void onDrawerClosed(View view) {
            getActionBar().setTitle("Freesound");
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerOpened(View view) {
            getActionBar().setTitle("Freesound");
            invalidateOptionsMenu();
        }
    }

}
