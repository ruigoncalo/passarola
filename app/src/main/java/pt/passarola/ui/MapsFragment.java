package pt.passarola.ui;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.Place;
import pt.passarola.services.BusProvider;
import pt.passarola.utils.Utils;
import pt.passarola.utils.dagger.DaggerableFragment;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 21/10/15.
 */
public class MapsFragment extends DaggerableFragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject MapsPresenter presenter;
    @Inject BusProvider busProvider;

    private GoogleMap map;
    private boolean googlePlayServicesAvailable;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private boolean isConnected;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_maps, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        googlePlayServicesAvailable = Utils.isGooglePlayServicesAvailable(getActivity());
        if (googlePlayServicesAvailable) {
            setUpMapIfNeeded();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        busProvider.register(this);

        if (!isConnected && googlePlayServicesAvailable) {
            if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        busProvider.unregister(this);

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view)).getMap();
        }
    }

    private void setUpMap() {
        LatLng currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentPosition)
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.clear();
        map.addMarker(new MarkerOptions().position(currentPosition).title("Current position"));
    }


    @Override
    public void onConnected(Bundle bundle) {
        Timber.d("onConnected");
        isConnected = true;
        if (googleApiClient != null) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (currentLocation != null && map != null) {
                setUpMap();
                presenter.getPlaces();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("onConnectionSuspended");
        googleApiClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed");
        googleApiClient = null;
    }

    @Subscribe
    public void onPlacesListEvent(ArrayList<Place> places) {
        for (Place place : places) {
            Double lat = Double.valueOf(place.getLat());
            Double lng = Double.valueOf(place.getLng());
            LatLng latLng = new LatLng(lat, lng);
            Timber.d("Adding marker for " + latLng.toString());
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(place.getName()));
        }
    }
}
