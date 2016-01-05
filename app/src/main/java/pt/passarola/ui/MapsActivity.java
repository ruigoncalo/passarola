package pt.passarola.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.MapItems;
import pt.passarola.model.viewmodel.MixedPlaceViewModel;
import pt.passarola.model.viewmodel.MixedViewModel;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.ui.components.MarkerToolbar;
import pt.passarola.ui.components.PassarolaToolbar;
import pt.passarola.ui.recyclerview.OnBaseItemClickListener;
import pt.passarola.ui.recyclerview.PlacesMixedAdapter;
import pt.passarola.utils.FeedbackManager;
import pt.passarola.services.dagger.DaggerableAppCompatActivity;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity implements OnMapReadyCallback,
        MapPresenterCallback, GoogleMap.OnInfoWindowClickListener, PassarolaToolbar.OnTabSelectedListener,
        GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnInfoWindowLongClickListener,
        MarkerToolbar.OnMarkerToolbarClickListener {

    private static final String BUNDLE_KEY_MAP_STATE = "bundle-map-state";

    @Inject MapsPresenter presenter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.map_view) MapView mapView;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.tabs_layout) TabLayout tabLayout;
    @Bind(R.id.layout_coordinator) ViewGroup coordinatorLayout;
    @Bind(R.id.layout_content) ViewGroup contentLayout;

    private PassarolaToolbar passarolaToolbar;
    private Location currentLocation;
    private GoogleMap googleMap;
    private PlacesMixedAdapter adapter;
    private Map<String, Marker> markersMap;
    private MarkerToolbar markerToolbar;

    private OnBaseItemClickListener onPlaceItemClick = new OnBaseItemClickListener() {
        @Override
        public void onBaseItemClick(int position, View view) {
            MixedPlaceViewModel mixedPlaceViewModel =
                    (MixedPlaceViewModel) adapter.getItem(position);
            Marker marker = markersMap.get(mixedPlaceViewModel.getPlaceViewModel().getId());
            centerMapOnLatLng(marker.getPosition());
            marker.showInfoWindow();
            dismissClosestPlaces();
        }
    };

    private OnBaseItemClickListener onTransparentItemClick = new OnBaseItemClickListener() {
        @Override
        public void onBaseItemClick(int position, View view) {
            dismissClosestPlaces();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_maps);
        ButterKnife.bind(this);
        markersMap = new ArrayMap<>();

        setupToolbar();
        setupTabs();
        setupMap(savedInstanceState);
        setupRecyclerView();
        setupMarkerToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
        adapter.registerClickListeners(onPlaceItemClick, onTransparentItemClick);
        passarolaToolbar.registerListener(this);
        markerToolbar.registerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        adapter.unregisterClickListeners();
        passarolaToolbar.unregisterListener();
        markerToolbar.unregisterListener();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the map state to it's own bundle
        Bundle mapState = new Bundle();
        if (mapView != null) {
            mapView.onSaveInstanceState(mapState);
        }
        // Put the map bundle in the main outState
        outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
        super.onSaveInstanceState(outState);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        resetToolbarTitle();
    }

    private void resetToolbarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    private void setToolbarTitle(int stringResource) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(stringResource);
        }
    }

    private void setupTabs() {
        passarolaToolbar = new PassarolaToolbar(tabLayout);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case PassarolaToolbar.TAB_CLOSEST_POSITION:
                onClosestTabClick();
                break;

            case PassarolaToolbar.TAB_PLACES_POSITION:
                onPlacesTabClick();
                break;

            default:
                onBeersTabClick();
                break;
        }
    }

    private void onClosestTabClick() {
        if (currentLocation != null) {
            presenter.getClosestPlaces(currentLocation);
        } else {
            refreshLocation();
        }
    }

    private void onPlacesTabClick() {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onBeersTabClick() {
        Intent intent = new Intent(this, BeersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setupMap(Bundle savedInstanceState) {
        Bundle mapState = null;
        if (savedInstanceState != null) {
            // Load the map state bundle from the main savedInstanceState
            mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
        }

        if (mapView != null) {
            mapView.onCreate(mapState);
            mapView.getMapAsync(this);
        }
    }

    private void setupMarkerToolbar() {
        markerToolbar = new MarkerToolbar(this);
    }

    @Override
    public void onMarkerToolbarIconClick(int position) {
        switch (position){
            case MarkerToolbar.ICON_1:
                break;

            case MarkerToolbar.ICON_2:
                break;

            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupGoogleMap();
    }

    private void setupGoogleMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnInfoWindowLongClickListener(this);
        googleMap.setOnInfoWindowCloseListener(this);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                presenter.getPlaces();
            }
        });
    }

    private void showLocationOnMap(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void showMarkers(List<PlaceViewModel> places) {
        if (googleMap != null && !places.isEmpty()) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (PlaceViewModel placeViewModel : places) {
                Marker marker = googleMap.addMarker(createMarkerOption(placeViewModel));
                boundsBuilder.include(marker.getPosition());
                markersMap.put(placeViewModel.getId(), marker);
            }
            LatLngBounds bounds = boundsBuilder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 160);
            googleMap.animateCamera(cameraUpdate);

        } else {
            onPlacesErrorEvent(new Exception("Google map is not instantiated"));
        }
    }

    private MarkerOptions createMarkerOption(PlaceViewModel placeViewModel) {
        return new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(placeViewModel.getLatLng())
                .snippet("Click me if you dare, Indiana!")
                .title(placeViewModel.getName());
    }

    private void refreshLocation() {
        presenter.getCurrentLocation();
    }

    private void setupRecyclerView() {
        adapter = new PlacesMixedAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        //hide view on create activity
        recyclerView.setVisibility(View.GONE);
    }

    private void loadClosestPlaces(List<MixedViewModel> list) {
        adapter.clearList();
        adapter.setItemList(list);
        showRecyclerView(true);
        setToolbarTitle(R.string.closest_places);
    }

    private void dismissClosestPlaces() {
        showRecyclerView(false);
        adapter.clearList();
        resetToolbarTitle();
    }

    private void showRecyclerView(boolean show) {
        passarolaToolbar.show(!show);

        if (show) {
            recyclerView.setAlpha(0);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .setListener(null);
        } else {
            recyclerView.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            recyclerView.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void centerMapOnLatLng(LatLng latlng) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
        }
    }

    private void showInfoView(String message, String actionMessage, View.OnClickListener clickListener) {
        FeedbackManager.showFeedbackIndeterminate(coordinatorLayout, message, actionMessage, clickListener);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Timber.d("Opening " + marker.getId());
        markerToolbar.show(contentLayout);
        passarolaToolbar.show(false);
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Timber.d("Long click " + marker.getId());
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Timber.d("Closing " + marker.getId());
        markerToolbar.hide();
        passarolaToolbar.show(true);
    }

    @Override
    public void onLocationSuccessEvent(Location location) {
        currentLocation = location;
        showLocationOnMap(location);
    }

    @Override
    public void onLocationErrorEvent(Exception e) {
        showInfoView(getString(R.string.error_location), getString(R.string.error_settings),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onPlacesSuccessEvent(MapItems mapItems) {
        showMarkers(mapItems.getPlaces());
        presenter.getCurrentLocation();
    }

    @Override
    public void onPlacesErrorEvent(Exception e) {
        showInfoView(getString(R.string.error_internet), getString(R.string.error_retry),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getPlaces();
                    }
                });
    }

    @Override
    public void onClosestPlacesSuccessEvent(List<MixedViewModel> list) {
        loadClosestPlaces(list);
    }

    @Override
    public void onClosestPlacesErrorEvent(Exception e) {
        showInfoView(getString(R.string.error_internet), getString(R.string.error_retry),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClosestTabClick();
                    }
                });
    }

    @Override
    public void isLoading(boolean loading) {

    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            dismissClosestPlaces();
        } else {
            super.onBackPressed();
        }
    }
}
