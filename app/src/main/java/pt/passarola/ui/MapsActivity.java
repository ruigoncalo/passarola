package pt.passarola.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.utils.Utils;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 21/10/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject MapsPresenter presenter;

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
        setSupportActionBar(toolbar);

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
        if(googleApiClient != null){
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
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
        map.addMarker(new MarkerOptions().position(currentPosition).title("Current position").alpha(0.7f));
    }


    @Override
    public void onConnected(Bundle bundle) {
        isConnected = true;
        if (googleApiClient != null) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(currentLocation != null && map != null){
                setUpMap();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        googleApiClient = null;
    }

}
