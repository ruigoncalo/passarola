package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.passarola.R;
import pt.passarola.model.viewmodel.MixedPlaceViewModel;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class PlacesMixedAdapter extends MixedBaseAdapter<ClosestPlacesViewHolder, TransparentViewHolder> {

    @Override
    public ClosestPlacesViewHolder onCreateViewHolderA(
            ViewGroup viewGroup, int viewType, OnBaseItemClickListener onBaseItemClickListener) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_item_closest_place, viewGroup, false);
        return new ClosestPlacesViewHolder(view, onBaseItemClickListener);
    }

    @Override
    public TransparentViewHolder onCreateViewHolderB(
            ViewGroup viewGroup, int viewType, OnBaseItemClickListener onBaseItemClickListener) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_item_transparent, viewGroup, false);
        return new TransparentViewHolder(view, onBaseItemClickListener);
    }

    @Override
    public void onBindViewHolderA(RecyclerView.ViewHolder holder, int position) {
        MixedPlaceViewModel mixedPlaceViewModel = (MixedPlaceViewModel) getItem(position);
        ((ClosestPlacesViewHolder) holder).compose(mixedPlaceViewModel.getPlaceViewModel(), position);
    }

    @Override
    public void onBindViewHolderB(RecyclerView.ViewHolder holder, int position) {
        ((TransparentViewHolder) holder).compose(getItem(position), position);
    }
}
