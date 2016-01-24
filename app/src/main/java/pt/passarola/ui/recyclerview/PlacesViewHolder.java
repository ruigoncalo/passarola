package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.ui.components.PlaceToolbarManager;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class PlacesViewHolder  extends RecyclerView.ViewHolder implements Composer<PlaceViewModel> {

    @Bind(R.id.text_name) TextView nameTextView;
    @Bind(R.id.text_address) TextView addressTextView;
    @Bind(R.id.text_council) TextView councilTextView;
    @Bind(R.id.text_country) TextView countryTextView;
    @Bind(R.id.button_place_facebook) View facebookButton;
    @Bind(R.id.button_place_zomato) View zomatoButton;
    @Bind(R.id.button_place_tripadvisor) View tripadvisorButton;

    private PlaceToolbarManager.OnPlaceToolbarClickListener listener;

    public PlacesViewHolder(View view, PlaceToolbarManager.OnPlaceToolbarClickListener l) {
        super(view);
        this.listener = l;
        ButterKnife.bind(this, view);
    }

    @Override
    public void compose(final PlaceViewModel placeViewModel, final int position) {
        nameTextView.setText(placeViewModel.getName());
        addressTextView.setText(placeViewModel.getFullAddress());
        councilTextView.setText(placeViewModel.getCouncil());
        countryTextView.setText(placeViewModel.getCountry());

        setClickListener(facebookButton, placeViewModel.getFacebook(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPlaceToolbarFacebookClick(placeViewModel.getFacebook());
                }
            }
        });

        setClickListener(zomatoButton, placeViewModel.getZomato(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPlaceToolbarZomatoClick(placeViewModel.getZomato());
                }
            }
        });

        setClickListener(tripadvisorButton, placeViewModel.getTripadvisor(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPlaceToolbarTripadvisorClick(placeViewModel.getTripadvisor());
                }
            }
        });


    }

    private void setClickListener(View view, String link, View.OnClickListener listener){
        if(link == null || link.isEmpty()){
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(listener);
        }
    }
}
