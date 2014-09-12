package grawlix.freesound.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import grawlix.freesound.R;
import grawlix.freesound.Resources.Result;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by luismierez on 8/3/14.
 */
public class SoundAdapter extends ArrayAdapter<Result> {

    private LayoutInflater mInflater;

    public SoundAdapter(Context context, int resource, List<Result> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            // View doesn't exist so create it and create the holder
            view = mInflater.inflate(R.layout.search_list_layout, parent, false);

            holder = new Holder();
            holder.soundName = (TextView) view.findViewById(R.id.sound_name);
            holder.soundId = (TextView) view.findViewById(R.id.sound_id);

            view.setTag(holder);
        } else {
            // Just get our existing holder
            holder = (Holder) view.getTag();
        }

        // Populate via the holder for speed
        Result result = getItem(position);


        // populate the item contents
        //Log.d("result", result.getName());
        holder.soundName.setText(result.getName());
        holder.soundId.setText(String.valueOf(result.getId()));

        return view;

    }

    // Holder class used to efficiently recycle view positions
    private static final class Holder {
        public TextView soundName;
        public TextView soundId;
        public CardView card;
    }
}
