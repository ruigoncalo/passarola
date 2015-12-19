package pt.passarola.ui;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.MapItems;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.utils.Callback;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity
        implements OnMapReadyCallback, Callback<MapItems> {

    private static final String BUNDLE_KEY_MAP_STATE = "bundle-map-state";

    @Inject MapsPresenter presenter;

    @Bind(R.id.map_view) MapView mapView;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_maps);
        ButterKnife.bind(this);
        setupMap(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the map state to it's own bundle
        Bundle mapState = new Bundle();
        mapView.onSaveInstanceState(mapState);
        // Put the map bundle in the main outState
        outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
        super.onSaveInstanceState(outState);
    }

    private void setupMap(Bundle savedInstanceState){
        Bundle mapState = null;
        if (savedInstanceState != null) {
            // Load the map state bundle from the main savedInstanceState
            mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
        }
        mapView.onCreate(mapState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupGoogleMap();
        presenter.getCurrentLocation();
    }

    private void setupGoogleMap(){
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void showLocationOnMap(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    private void showMarkers(List<PlaceViewModel> places){
        for(PlaceViewModel placeViewModel : places){
            googleMap.addMarker(createMarkerOption(placeViewModel));
        }
    }

    private MarkerOptions createMarkerOption(PlaceViewModel placeViewModel){
        return new MarkerOptions()
                .position(placeViewModel.getLatLng())
                .title(placeViewModel.getName())
                .snippet(placeViewModel.getId());
    }

    @Override
    public void onLocationSuccessEvent(Location location) {
        showLocationOnMap(location);
        presenter.getPlaces();
    }

    @Override
    public void onLocationErrorEvent(Exception e) {

    }

    @Override
    public void onPlacesSuccessEvent(MapItems mapItems){
        showMarkers(mapItems.getPlaces());
    }

    @Override
    public void onPlacesErrorEvent(Exception e) {

    }

    @Override
    public void isLoading(boolean loading) {

    }
}
