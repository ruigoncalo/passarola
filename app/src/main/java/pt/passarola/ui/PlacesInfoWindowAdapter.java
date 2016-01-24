package pt.passarola.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import pt.passarola.R;

/**
 * Created by ruigoncalo on 24/01/16.
 */
public class PlacesInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View contentsView;

    public PlacesInfoWindowAdapter(LayoutInflater layoutInflater) {
        contentsView = layoutInflater.inflate(R.layout.layout_info_content_places, null);
    }

    // customize info contents rather than the entire window
    @Override
    public View getInfoContents(Marker marker) {
        TextView titleView = (TextView) contentsView.findViewById(R.id.text_info_window_title);
        titleView.setText(marker.getTitle());
        return contentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
