package pt.passarola.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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

import java.util.HashMap;
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
import pt.passarola.ui.components.MarkerToolbarManager;
import pt.passarola.ui.components.PassarolaToolbarManager;
import pt.passarola.ui.recyclerview.OnBaseItemClickListener;
import pt.passarola.ui.recyclerview.PlacesMixedAdapter;
import pt.passarola.utils.AnimatorManager;
import pt.passarola.utils.FeedbackManager;
import pt.passarola.services.dagger.DaggerableAppCompatActivity;
import pt.passarola.utils.ScreenInspector;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity implements OnMapReadyCallback,
        MapPresenterCallback, GoogleMap.OnInfoWindowClickListener, PassarolaToolbarManager.OnTabSelectedListener,
        GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnInfoWindowLongClickListener,
        MarkerToolbarManager.OnMarkerToolbarClickListener {

    private static final String BUNDLE_KEY_MAP_STATE = "bundle-map-state";

    @Inject MapsPresenter presenter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.map_view) MapView mapView;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.tabs_layout) TabLayout tabLayout;
    @Bind(R.id.layout_coordinator) ViewGroup coordinatorLayout;
    @Bind(R.id.layout_marker_toolbar) ViewGroup markerToolbarLayout;

    private PassarolaToolbarManager passarolaToolbarManager;
    private Location currentLocation;
    private GoogleMap googleMap;
    private PlacesMixedAdapter adapter;
    private Map<PlaceViewModel, Marker> markersMap;
    private MarkerToolbarManager markerToolbarManager;
    private int recyclerViewHeight;

    private OnBaseItemClickListener onPlaceItemClick = new OnBaseItemClickListener() {
        @Override
        public void onBaseItemClick(int position, View view) {
            MixedPlaceViewModel mixedPlaceViewModel =
                    (MixedPlaceViewModel) adapter.getItem(position);
            Marker marker = markersMap.get(mixedPlaceViewModel.getPlaceViewModel());
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
        markersMap = new HashMap<>();

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
        passarolaToolbarManager.registerListener(this);
        markerToolbarManager.registerListener(this);
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
        passarolaToolbarManager.unregisterListener();
        markerToolbarManager.unregisterListener();
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
        passarolaToolbarManager = new PassarolaToolbarManager(tabLayout);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case PassarolaToolbarManager.TAB_CLOSEST_POSITION:
                onClosestTabClick();
                break;

            case PassarolaToolbarManager.TAB_PLACES_POSITION:
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
        markerToolbarManager = new MarkerToolbarManager(this);
    }

    @Override
    public void onMarkerToolbarIconClick(int position) {
        switch (position){
            case MarkerToolbarManager.ICON_1:
                break;

            case MarkerToolbarManager.ICON_2:
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
                markersMap.put(placeViewModel, marker);
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

        // height = screen height - (status bar + toolbar)
        recyclerViewHeight = ScreenInspector.getScreenHeightPx(this)
                - ScreenInspector.getAppBarHeightPx(this)
                - ScreenInspector.getStatusBarHeightPx(this);
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
        passarolaToolbarManager.show(!show);

        if (show) {
            recyclerView.setTranslationY(recyclerViewHeight);
            if(mapView != null){
                AnimatorManager.fadeOutPartial(mapView);
            }
            AnimatorManager.slideInView(recyclerView, 0);
        } else {
            if(mapView != null){
                AnimatorManager.fadeInPartial(mapView);
            }
            AnimatorManager.slideOutView(recyclerView, recyclerViewHeight);
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

    @Nullable
    private PlaceViewModel getPlaceViewModelFromMap(Marker marker){
        for(Map.Entry<PlaceViewModel, Marker> entry : markersMap.entrySet()){
            if(entry.getValue().getId().equals(marker.getId())){
                return entry.getKey();
            }
        }

        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PlaceViewModel placeViewModel = getPlaceViewModelFromMap(marker);
        markerToolbarManager.show(markerToolbarLayout, placeViewModel);
        passarolaToolbarManager.show(false);
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Timber.d("Long click " + marker.getId());
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Timber.d("Closing " + marker.getId());
        markerToolbarManager.hide();
        passarolaToolbarManager.show(true);
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
