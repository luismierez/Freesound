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
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
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
        Result result = getItem(position);

        if (view == null) {
            // View doesn't exist so create it and create the holder
            view = mInflater.inflate(R.layout.search_list_layout, parent, false);

            holder = new Holder();
            holder.card = (CardView) view.findViewById(R.id.list_sound_card_view);

            CardHeader header = new CardHeader(getContext());
            header.setTitle(result.getName());
            Card mCard = new Card(getContext());
            mCard.addCardHeader(header);
            holder.card.setCard(mCard);

            view.setTag(holder);
        } else {
            // Just get our existing holder
            holder = (Holder) view.getTag();
        }

        // Populate via the holder for speed

        CardHeader header = new CardHeader(getContext());
        header.setTitle(result.getName());
        Card mCard = new Card(getContext());
        mCard.addCardHeader(header);
        holder.card.refreshCard(mCard);

        // populate the item contents



        return view;

    }

    // Holder class used to efficiently recycle view positions
    private static final class Holder {

        public CardView card;
    }
}
