package pt.passarola.ui.components;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.passarola.R;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.ui.SocialPlacesListener;
import pt.passarola.utils.AnimatorManager;
import pt.passarola.utils.ScreenInspector;

/**
 * Created by ruigoncalo on 04/01/16.
 */
public class PlaceToolbarManager {

    private Context context;
    private View placeToolbar;
    private SocialPlacesListener listener;
    private int height;

    public PlaceToolbarManager(Context context) {
        this.context = context;
    }

    public void registerListener(SocialPlacesListener listener) {
        this.listener = listener;
    }

    public void unregisterListener() {
        this.listener = null;
    }

    public void show(ViewGroup anchor, PlaceViewModel placeViewModel) {
        if (placeToolbar == null) {
            placeToolbar = LayoutInflater.from(context).inflate(R.layout.layout_place_detail, anchor, true);

            if (placeToolbar == null) {
                throw new NullPointerException("Place toolbar could not be instantiated");
            }

            height = (int) ScreenInspector.dpToPx(placeToolbar.getContext(), 190);
        }

        bind(placeViewModel);

        // show animation
        // translate y to hide the view
        // and then animate as sliding up
        placeToolbar.setTranslationY(height);
        AnimatorManager.slideInView(placeToolbar, 0);
    }

    public void hide() {
        if (placeToolbar != null && height != 0) {
            // sliding down
            AnimatorManager.slideOutView(placeToolbar, height);
        }
    }

    private void bind(final PlaceViewModel placeViewModel){
        TextView nameView = (TextView) placeToolbar.findViewById(R.id.text_name);
        TextView addressView = (TextView) placeToolbar.findViewById(R.id.text_address);
        TextView councilView = (TextView) placeToolbar.findViewById(R.id.text_council);
        TextView countryView = (TextView) placeToolbar.findViewById(R.id.text_country);

        nameView.setText(placeViewModel.getName());
        addressView.setText(placeViewModel.getFullAddress());
        councilView.setText(placeViewModel.getCouncil());
        countryView.setText(placeViewModel.getCountry());

        View facebookButton = placeToolbar.findViewById(R.id.button_place_facebook);
        if (facebookButton != null) {
            if(placeViewModel.getFacebook() == null || placeViewModel.getFacebook().isEmpty()){
                facebookButton.setVisibility(View.GONE);
            } else {
                facebookButton.setVisibility(View.VISIBLE);
                facebookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onFacebookClick(placeViewModel.getFacebook());
                        }
                    }
                });
            }
        }

        View zomatoButton = placeToolbar.findViewById(R.id.button_place_zomato);
        if (zomatoButton != null) {
            if(placeViewModel.getZomato() == null || placeViewModel.getZomato().isEmpty()){
                zomatoButton.setVisibility(View.GONE);
            } else {
                zomatoButton.setVisibility(View.VISIBLE);
                zomatoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onZomatoClick(placeViewModel.getZomato());
                        }
                    }
                });
            }
        }

        View tripadvisorButton = placeToolbar.findViewById(R.id.button_place_tripadvisor);
        if (tripadvisorButton != null) {
            if(placeViewModel.getTripadvisor() == null || placeViewModel.getTripadvisor().isEmpty()){
                tripadvisorButton.setVisibility(View.GONE);
            } else {
                tripadvisorButton.setVisibility(View.VISIBLE);
                tripadvisorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onTripadvisorClick(placeViewModel.getTripadvisor());
                        }
                    }
                });
            }
        }

        FloatingActionButton phoneButton =
                (FloatingActionButton) placeToolbar.findViewById(R.id.button_place_phone);
        if(phoneButton != null) {
            if (placeViewModel.getTelephone() == null || placeViewModel.getTelephone().isEmpty()) {
                phoneButton.setVisibility(View.GONE);
            } else {
                phoneButton.setVisibility(View.VISIBLE);
                phoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onPhoneClick(placeViewModel.getTelephone());
                        }
                    }
                });
            }
        }
    }

}
