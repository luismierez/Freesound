package grawlix.freesound.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Result;
import grawlix.freesound.Resources.Sound;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 10/9/14.
 */
public class RSoundAdapter extends RecyclerView.Adapter<RSoundAdapter.ViewHolder> {

    private List<Result> mResults;
    private int rowLayout;
    private Context mContext;

    // Provide a reference to the type of views that you are using
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView soundName;
        public ImageView soundImage;
        public ViewHolder(View itemView) {
            super(itemView);
            soundName = (TextView) itemView.findViewById(R.id.soundName);
            soundImage = (ImageView) itemView.findViewById(R.id.soundImage);
        }

    }

    // Provide a suitable constructor
    public RSoundAdapter(List<Result> results, int rowLayout, Context context) {
        mResults = results;
        this.rowLayout = rowLayout;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RSoundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Result result = mResults.get(position);
        holder.soundName.setText(result.getName());

        Picasso.with(mContext).load(result.getImages().getWaveformM()).fit().into(holder.soundImage);
    }

    @Override
    public int getItemCount() {
        return mResults == null ? 0 : mResults.size();
    }
}
