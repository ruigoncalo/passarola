package pt.passarola.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.passarola.R;
import pt.passarola.model.viewmodel.BeerViewModel;
import pt.passarola.ui.SocialBeerListener;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeersAdapter extends BaseAdapter<BeerViewModel, BeersViewHolder> {

    private SocialBeerListener listener;

    public BeersAdapter(Context context){
        super(context);
    }


    public void registerListener(SocialBeerListener listener){
        this.listener = listener;
    }

    public void unregisterListener(){
        this.listener = null;
    }

    @Override
    public BeersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View editView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_item_beer, viewGroup, false);
        return new BeersViewHolder(editView, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeerViewModel item = getItem(position);
        ((BeersViewHolder) holder).compose(item, position);
    }
}
