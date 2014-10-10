package grawlix.freesound.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import grawlix.freesound.Adapters.RSoundAdapter;
import grawlix.freesound.Controllers.MusicController;
import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.Listener.RecyclerItemTouchListener;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Result;
import grawlix.freesound.Resources.SearchText;
import grawlix.freesound.Services.MusicService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 8/5/14.
 */
public class ResultsFragment extends Fragment implements Button.OnClickListener,
                                                         MediaController.MediaPlayerControl{

    ResultsCommunicator communicator;
    List<Result> results = new ArrayList<Result>();
    Button btn_next;
    Button btn_previous;
    private int nextPage = 1;
    String search_query;
    String sort;

    private RecyclerView mRecyclerView;
    private RSoundAdapter mRSoundAdaper;

    private View mLoadingView;

    private int mShortAnimationDuration;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    private MusicController controller;

    private boolean paused=false, playbackPaused=false;

    private int currentSelectedSound = 0;

    private void setController() {
        // set the controller up
        controller = new MusicController(getActivity());

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(mRecyclerView);
        controller.setEnabled(true);
    }

    private void playNext() {
        if (currentSelectedSound<=results.size()) {
            selectItem(++currentSelectedSound);
        }
        if (playbackPaused) {
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev() {
        if (currentSelectedSound>=0) {
            selectItem(--currentSelectedSound);
        }
        if(playbackPaused) {
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            // get service
            musicService = binder.getService();
            // pass id
            musicService.setSongId(1111);
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
        if (playIntent==null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        paused=true;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            communicator = (ResultsCommunicator) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ResultsCommunicator");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        search_query = getArguments().getString("SEARCH_QUERY");
        // Make sure that we gave a sort argument to the fragment
        if (getArguments().getString("SORT")!=null)
            sort = getArguments().getString("SORT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.results_fragment_layout, container, false);

        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_previous = (Button) view.findViewById(R.id.btn_previous);
        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);


        // recycler and card view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.results_recycler_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRSoundAdaper = new RSoundAdapter(results, R.layout.search_cardview_list_layout, getActivity());
        mRecyclerView.setAdapter(mRSoundAdaper);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemTouchListener(getActivity(), new RecyclerItemTouchListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "Clicked Item " + position, Toast.LENGTH_SHORT).show();
                        selectItem(position);
                        currentSelectedSound = position;
                    }
                })
        );

        // Load loading screen
        mLoadingView = view.findViewById(R.id.loading_spinner);
        // Hide the recycler view while it is loading
        mRecyclerView.setVisibility(View.GONE);
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setController();

        return view;
    }

    private void selectItem(int position) {
        musicService.setSongId(results.get(position).getId());
        musicService.playSong();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused=false;
        }
        if (getArguments().getString("SORT")!=null) {
            downloadData(search_query, nextPage, sort);
        } else {
            downloadData(search_query);
        }
    }

    @Override
    public void onStop() {
        controller.hide();
        super.onStop();
    }
    private void downloadData(String searchTerm) {
        showContentOrLoadingIndicator(false);
        FreesoundClient.getFreesoundApiClient().searchText(searchTerm, new Callback<SearchText>() {
            @Override
            public void success(SearchText searchText, Response response) {
                consumeApiData(searchText);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                consumeApiData(null);
            }
        });
    }

    private void downloadData(String searchTerm, int page) {
        showContentOrLoadingIndicator(false);
        FreesoundClient.getFreesoundApiClient().searchText(searchTerm, page, new Callback<SearchText>() {
            @Override
            public void success(SearchText searchText, Response response) {
                consumeApiData(searchText);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                consumeApiData(null);
            }
        });
    }

    private void downloadData(String searchTerm, int page, String sortTerm) {
        showContentOrLoadingIndicator(false);
        FreesoundClient.getFreesoundApiClient().searchText(searchTerm, page, sortTerm, new Callback<SearchText>() {
            @Override
            public void success(SearchText searchText, Response response) {
                consumeApiData(searchText);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                consumeApiData(null);
            }
        });
    }

    private void consumeApiData(SearchText searchText) {
        if (searchText != null) {
            results.clear();
            results.addAll(searchText.getResults());

            mRSoundAdaper.notifyDataSetChanged();
            showContentOrLoadingIndicator(true);
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next: {
                nextPage++;
                if (getArguments().getString("SORT")!= null) {
                    downloadData(search_query, nextPage, sort);
                } else {
                    downloadData(search_query, nextPage);
                }

                break;
            }

            case R.id.btn_previous: {
                nextPage--;
                if (getArguments().getString("SORT")!= null) {
                    downloadData(search_query, nextPage, sort);
                } else {
                    downloadData(search_query, nextPage);
                }

            }

        }
    }

    /**
     * Crossfades between the listview and the loadingview
     */
    private void showContentOrLoadingIndicator(boolean contentLoaded) {
        // Decide which view to hide or show
        final View showView = contentLoaded ? mRecyclerView : mLoadingView;
        final View hideView = contentLoaded ? mLoadingView : mRecyclerView;

        // Set the "show" view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation
        //showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        // get the final radius for the clipping circle
        int cx = (showView.getLeft() + showView.getRight()) / 2;
        int cy = (showView.getTop() + showView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = showView.getWidth();

        // create and start the animator for this view (the start radius is zero)
        ValueAnimator animShow = ViewAnimationUtils.createCircularReveal(showView, cx, cy, 0, finalRadius);
        animShow.start();

        cx = (hideView.getLeft() + hideView.getRight()) / 2;
        cy = (hideView.getTop() + hideView.getBottom()) / 2;

        int initialRadius = hideView.getWidth();

        // create the animation ( the final radius is zero )
        ValueAnimator animHide =
                ViewAnimationUtils.createCircularReveal(hideView, cx, cy, initialRadius, 0);

        animHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                hideView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animHide.start();


        /*

        // Animate the "show" view to 100% opacity, and clear any animation listener set on
        // the view. Remember that listeners are not limited to the specific animation
        // describes in the chained method calls. Listeners are set on the
        // ViewPropertyAnimator object for the view, which persists across several
        // animations.
        showView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        hideView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }

                });
        */
    }

    @Override
    public void start() {
        musicService.start();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
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
        musicService.seek(i);
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && musicBound)
            return musicService.isPlaying();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    // Used to send information to activity
    public interface ResultsCommunicator {
        public void respond(int soundId);
    }
}
