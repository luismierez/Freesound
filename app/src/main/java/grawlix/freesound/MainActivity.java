package grawlix.freesound;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.Resources.SearchResult;
import grawlix.freesound.Resources.Sound;


public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawer;
    private TextView mTestText;
    private int mSelectedFragment;
    private CustomActionBarDrawerToggle mDrawerToggle;
    private String[] test = {"Home", "Random Sound of the Day", "Most Downloaded Sounds"};
    private FreesoundClient client = FreesoundClient.getInstance();

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

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        mTestText = (TextView) findViewById(R.id.testText);
        mDrawerList = (ListView) findViewById(R.id.drawer);

        // Set the adapter for drawer list
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_layout, test));

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
        mDrawer.setDrawerListener(mDrawerToggle);
        // Freesound API key
        client.setClientSecret("97cd22ae047813db794abfb26de7a43273e0d5f6");
        //new GetSounds().execute();
        new doSearch().execute();
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

    private class GetSounds extends AsyncTask<Void, Void, Void> {

        String soundName;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Sound sound = client.getSound("1234", params);
            Log.d("doInBackground", sound.getName());
            soundName = sound.getName();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mTestText.setText(soundName);
        }
    }

    private class doSearch extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            SearchResult searchResult = client.textSearch("cars", params);
            for (int i = 0; i < searchResult.getResultsSize(); i++) {
                Log.d("Search id: " + i, searchResult.getSound(i).getName());
            }

            return null;
        }
    }
}
