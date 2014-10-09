package grawlix.freesound.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import grawlix.freesound.Adapters.SoundAdapter;
import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Result;
import grawlix.freesound.Resources.SearchText;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 8/5/14.
 */
public class ResultsFragment extends Fragment implements AdapterView.OnItemClickListener, Button.OnClickListener {


    AbsListView resultsList;
    ResultsCommunicator communicator;
    List<Result> results = new ArrayList<Result>();
    SoundAdapter mAdapter;
    Button btn_next;
    Button btn_previous;
    private int nextPage = 1;
    String search_query;
    String sort;


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
        mAdapter = new SoundAdapter(getActivity(), R.layout.search_list_layout, results);
        if (resultsList == null) {
            Log.d("onCreate", "resultsList is null");
        }
        search_query = getArguments().getString("SEARCH_QUERY");
        // Make sure that we gave a sort argument to the fragment
        if (getArguments().getString("SORT")!=null)
            sort = getArguments().getString("SORT");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.results_fragment_layout, container, false);
        resultsList = (AbsListView) view.findViewById(R.id.results_list);
        resultsList.setOnItemClickListener(this);
        resultsList.setAdapter(mAdapter);

        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_previous = (Button) view.findViewById(R.id.btn_previous);
        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);

        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (getActivity()!=null) {
            resultsList.setItemChecked(i, true);
            communicator.respond(results.get(i).getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().getString("SORT")!=null) {
            downloadData(search_query, nextPage, sort);
        } else {
            downloadData(search_query);
        }


    }

    private void downloadData(String searchTerm) {
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

            mAdapter.notifyDataSetChanged();
            resultsList.smoothScrollToPosition(0);
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

    // Used to send information to activity
    public interface ResultsCommunicator {
        public void respond(int soundId);
    }
}
