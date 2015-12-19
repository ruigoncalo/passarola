package pt.passarola.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import pt.passarola.model.events.LocationErrorEvent;
import pt.passarola.model.events.LocationSuccessEvent;

import static com.google.android.gms.common.ConnectionResult.SUCCESS;


/**
 * Created by ruigoncalo on 19/12/15.
 */
public class LocationProvider
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private BusProvider busProvider;
    private GoogleApiClient googleApiClient;

    public LocationProvider(BusProvider busProvider, Context context) {
        this.busProvider = busProvider;
        this.context = context;
    }

    public void getCurrentLocation() {
        if (isGooglePlayServicesAvailable()) {
            createGoogleApiClient();
            googleApiClient.connect();
        } else {
            postError(new Exception("Google Play Services not available"));
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == SUCCESS;
    }

    private void createGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void postError(Exception e) {
        busProvider.post(new LocationErrorEvent(e));
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(googleApiClient != null) {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            googleApiClient.disconnect();
            if (currentLocation != null) {
                busProvider.post(new LocationSuccessEvent(currentLocation));
            } else {
                postError(new Exception("Unable to find current location"));
            }
        } else {
            postError(new Exception("Invalid Google Api Client"));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient = null;
        postError(new Exception("Google Api Client connection suspended"));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        googleApiClient = null;
        postError(new Exception(connectionResult.getErrorMessage()));
    }

}
