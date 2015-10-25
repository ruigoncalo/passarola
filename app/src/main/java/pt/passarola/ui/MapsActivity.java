package pt.passarola.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.Place;
import pt.passarola.services.BusProvider;
import pt.passarola.utils.Utils;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 21/10/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject MapsPresenter presenter;
    @Inject BusProvider busProvider;

    @Bind(R.id.toolbar) Toolbar toolbar;

    private GoogleMap map;
    private boolean googlePlayServicesAvailable;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setupToolbar();

        googlePlayServicesAvailable = Utils.isGooglePlayServicesAvailable(this);
        if(googlePlayServicesAvailable) {
            setUpMapIfNeeded();
        } else {
            Snackbar.make(toolbar, "Google Play Services not available", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
        busProvider.register(this);

        if (!isConnected && googlePlayServicesAvailable) {
            if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop(){
        presenter.stop();
        busProvider.unregister(this);

        if(googleApiClient != null){
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.finding_passarola);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
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
            if(currentLocation != null && map != null){
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
    public void onPlacesListEvent(ArrayList<Place> places){
        for(Place place : places){
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
