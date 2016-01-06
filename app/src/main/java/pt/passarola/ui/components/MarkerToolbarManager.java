package pt.passarola.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.passarola.R;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.utils.AnimatorManager;
import pt.passarola.utils.ScreenInspector;

/**
 * Created by ruigoncalo on 04/01/16.
 */
public class MarkerToolbarManager {

    public static final int ICON_1 = 1;
    public static final int ICON_2 = 2;
    public static final int ICON_3 = 3;

    private Context context;
    private View markerToolbar;
    private OnMarkerToolbarClickListener listener;
    private int height;

    private TextView nameView;
    private TextView addressView;
    private TextView councilView;
    private TextView countryView;

    public MarkerToolbarManager(Context context) {
        this.context = context;
    }

    public void registerListener(OnMarkerToolbarClickListener listener) {
        this.listener = listener;
    }

    public void unregisterListener() {
        this.listener = null;
    }

    public void show(ViewGroup anchor, PlaceViewModel placeViewModel) {
        if (markerToolbar == null) {
            markerToolbar = LayoutInflater.from(context).inflate(R.layout.marker_toolbar, anchor, true);

            if (markerToolbar == null) {
                throw new NullPointerException("Marker toolbar could not be instantiated");
            }

            height = (int) ScreenInspector.dpToPx(markerToolbar.getContext(), 150);
        }

        bind(placeViewModel);

        // show animation
        // translate y to hide the view
        // and then animate as sliding up
        markerToolbar.setTranslationY(height);
        AnimatorManager.slideInView(markerToolbar, 0);
    }

    public void hide() {
        if (markerToolbar != null && height != 0) {
            // sliding down
            AnimatorManager.slideOutView(markerToolbar, height);
        }
    }

    private void bind(PlaceViewModel placeViewModel){
        nameView = (TextView) markerToolbar.findViewById(R.id.text_name);
        addressView = (TextView) markerToolbar.findViewById(R.id.text_address);
        councilView = (TextView) markerToolbar.findViewById(R.id.text_council);
        countryView = (TextView) markerToolbar.findViewById(R.id.text_country);

        nameView.setText(placeViewModel.getName());
        addressView.setText(placeViewModel.getFullAddress());
        councilView.setText(placeViewModel.getCouncil());
        countryView.setText(placeViewModel.getCountry());

        View icon1 = markerToolbar.findViewById(R.id.icon_marker_1);
        if (icon1 != null) {
            setClickListener(ICON_1, icon1);
        }

        View icon2 = markerToolbar.findViewById(R.id.icon_marker_2);
        if (icon2 != null) {
            setClickListener(ICON_2, icon2);
        }

        View icon3 = markerToolbar.findViewById(R.id.icon_marker_3);
        if (icon3 != null) {
            setClickListener(ICON_3, icon3);
        }
    }

    private void setClickListener(final int position, View icon) {
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMarkerToolbarIconClick(position);
                }
            }
        });
    }

    public interface OnMarkerToolbarClickListener {
        void onMarkerToolbarIconClick(int position);
    }

}
