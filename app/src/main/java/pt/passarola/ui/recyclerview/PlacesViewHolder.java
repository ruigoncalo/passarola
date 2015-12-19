package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.viewmodel.PlaceViewModel;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class PlacesViewHolder  extends RecyclerView.ViewHolder implements Composer<PlaceViewModel>, View.OnClickListener {

    @Bind(R.id.text_name) TextView nameTextView;
    @Bind(R.id.text_address) TextView addressTextView;
    @Bind(R.id.text_council) TextView councilTextView;
    @Bind(R.id.text_country) TextView countryTextView;
    @Bind(R.id.text_telephone) TextView telephoneTextView;

    private BaseAdapter.OnBaseItemClickListener onItemClickListener;

    public PlacesViewHolder(View view, BaseAdapter.OnBaseItemClickListener onItemClickListener) {
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
        councilTextView.setText(placeViewModel.getCouncil());
        countryTextView.setText(placeViewModel.getCountry());
        telephoneTextView.setText(placeViewModel.getTelephone());
    }
}
