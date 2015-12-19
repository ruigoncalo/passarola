package pt.passarola.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.passarola.R;
import pt.passarola.model.viewmodel.BeerViewModel;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeersAdapter extends BaseAdapter<BeerViewModel, BeersViewHolder> {

    private OnBaseItemClickListener onBaseItemClickListener;

    public BeersAdapter(Context context){
        super(context);
    }

    public void registerBaseItemClickListener(OnBaseItemClickListener onBaseItemClickListener){
        this.onBaseItemClickListener = onBaseItemClickListener;
    }

    public void unregisterBaseItemClickListener(){
        this.onBaseItemClickListener = null;
    }

    @Override
    public BeersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View editView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_beer, viewGroup, false);
        return new BeersViewHolder(editView, onBaseItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeerViewModel item = getItem(position);
        ((BeersViewHolder) holder).compose(item, position);
    }
}
