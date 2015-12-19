package pt.passarola.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pt.passarola.R;
import pt.passarola.model.viewmodel.PlaceViewModel;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesAdapter extends BaseAdapter<PlaceViewModel, PlacesViewHolder> {

    private OnBaseItemClickListener onBaseItemClickListener;

    public PlacesAdapter(Context context){
        super(context);
    }

    public void registerBaseItemClickListener(OnBaseItemClickListener onBaseItemClickListener){
        this.onBaseItemClickListener = onBaseItemClickListener;
    }

    public void unregisterBaseItemClickListener(){
        this.onBaseItemClickListener = null;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View editView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_place, viewGroup, false);
        return new PlacesViewHolder(editView, onBaseItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlaceViewModel item = getItem(position);
        ((PlacesViewHolder) holder).compose(item, position);
    }

}
