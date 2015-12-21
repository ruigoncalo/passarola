package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.utils.RandomJokesGenerator;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class ClosestPlacesViewHolder extends RecyclerView.ViewHolder implements Composer<PlaceViewModel>, View.OnClickListener {

    @Bind(R.id.text_name) TextView nameTextView;
    @Bind(R.id.text_address) TextView addressTextView;
    @Bind(R.id.text_distance) TextView distanceTextView;

    private OnBaseItemClickListener onItemClickListener;

    public ClosestPlacesViewHolder(View view, OnBaseItemClickListener onItemClickListener) {
        super(view);
        this.onItemClickListener = onItemClickListener;
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
        if (onItemClickListener != null) {
            onItemClickListener.onBaseItemClick(position, view);
        }
    }

    @Override
    public void compose(final PlaceViewModel placeViewModel, final int position) {
        nameTextView.setText(placeViewModel.getName());
        addressTextView.setText(placeViewModel.getFullAddress());

        String distance = placeViewModel.getName() + " is " +
                placeViewModel.getDistance() + " meters far from you. " +
                RandomJokesGenerator.giveMeAJoke(placeViewModel.getDistance());

        distanceTextView.setText(distance);
    }
}

