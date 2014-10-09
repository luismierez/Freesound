package grawlix.freesound;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import grawlix.freesound.Fragments.ResultsFragment;
import grawlix.freesound.Fragments.SoundInstanceFragment;
import grawlix.freesound.Fragments.WelcomeFragment;
import grawlix.freesound.Services.MusicService;

public class MainActivity extends Activity implements ResultsFragment.ResultsCommunicator {

    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private String[] test = {"Home", "Random Sound of the Day", "Most Recent Sounds"};

    //------------Fragments----------------//
    FragmentManager manager;
    ResultsFragment mostRecentSoundsFragment;

    /*
        This method allows us to listen to ResultsFragment
     */
    @Override
    public void respond(int soundId) {

        Log.d("Main Activity respond", String.valueOf(soundId));

        // Create new intent with sound instance id
        Intent intent = new Intent(this, SoundInstanceActivity.class);
        intent.putExtra("SOUND_ID", soundId);
        startActivity(intent);

    }

    private static String BUNDLE_SELECTEDFRAGMENT = "BDL_SELFRG";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getFragmentManager();
        // Set the list fragment initially
        manager.beginTransaction().replace(R.id.fragment_main, new WelcomeFragment())
                .commit();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.drawer);

        // Set the adapter for drawer list
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_layout, test));
        // Set the click listener for the drawer list
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return super.onCreateOptionsMenu(menu);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    private class RetreiveFeaturedSound extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... strings) {
            String url = strings[0];
            Document document;
            try {
                document = Jsoup.connect(url).get();
                return document;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Document document) {
            Element featuredSound = document.getElementById("featured_sound");
            Elements randomSound = featuredSound.getElementsByClass("sample_player_small");
            String randomSoundId = "";
            for (Element e : randomSound) {
                String[] sArray = e.toString().split("\"");
                randomSoundId = sArray[3];
            }
            Log.d("featuredSound", randomSoundId);
            SoundInstanceFragment randomSoundFragment = new SoundInstanceFragment();
            Bundle args = new Bundle();
            args.putInt(SoundInstanceFragment.SOUND_ID_KEY, Integer.valueOf(randomSoundId));
            randomSoundFragment.setArguments(args);
            manager.beginTransaction().replace(R.id.fragment_main, randomSoundFragment)
                    .commit();
            mDrawer.closeDrawer(mDrawerList);
        }
    }



    private void selectItem(int position) {
        switch (position) {
            case 0: {
                WelcomeFragment welcomeFragment = new WelcomeFragment();
                manager.beginTransaction().replace(R.id.fragment_main,welcomeFragment)
                        .commit();
                mDrawer.closeDrawer(mDrawerList);
                break;
            }
            case 1: {
                // Random Sound of the Day fragment
                new RetreiveFeaturedSound().execute("http://www.freesound.org");
                break;
            }

            case 2: {
                // Most Recent sound
                mostRecentSoundsFragment = new ResultsFragment();
                Bundle args = new Bundle();
                args.putString("SEARCH_QUERY", "");
                args.putString("SORT", "created_desc");
                mostRecentSoundsFragment.setArguments(args);
                manager.beginTransaction().replace(R.id.fragment_main, mostRecentSoundsFragment).commit();
                mDrawer.closeDrawer(mDrawerList);


            }
        }
    }

}
