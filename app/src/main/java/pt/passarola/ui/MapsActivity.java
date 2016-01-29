package pt.passarola.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

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
import pt.passarola.services.tracker.TrackerManager;
import pt.passarola.ui.components.PlaceToolbarManager;
import pt.passarola.ui.components.PassarolaTabLayoutManager;
import pt.passarola.ui.recyclerview.OnBaseItemClickListener;
import pt.passarola.ui.recyclerview.PlacesMixedAdapter;
import pt.passarola.utils.AnimatorManager;
import pt.passarola.utils.FeedbackManager;
import pt.passarola.services.dagger.DaggerableAppCompatActivity;
import pt.passarola.utils.ScreenInspector;
import pt.passarola.utils.Utils;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity implements OnMapReadyCallback,
        MapPresenterCallback, GoogleMap.OnInfoWindowClickListener,
        PassarolaTabLayoutManager.OnTabSelectedListener, GoogleMap.OnInfoWindowCloseListener,
        SocialPlacesListener, GoogleMap.OnMarkerClickListener {

    private static final String BUNDLE_KEY_MAP_STATE = "bundle-map-state";

    @Inject MapsPresenter presenter;
    @Inject TrackerManager trackerManager;

    @Bind(R.id.map_view) MapView mapView;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.tabs_layout) TabLayout tabLayout;
    @Bind(R.id.layout_coordinator) ViewGroup coordinatorLayout;
    @Bind(R.id.layout_place_toolbar) ViewGroup placeToolbarLayout;

    private PassarolaTabLayoutManager passarolaTabLayoutManager;
    private Location currentLocation;
    private GoogleMap googleMap;
    private PlacesMixedAdapter adapter;
    private Map<LatLng, Pair<PlaceViewModel, Marker>> markersMap;
    private PlaceToolbarManager placeToolbarManager;
    private int recyclerViewHeight;
    private boolean isClosestPlaceMode;
    private boolean isPlaceToolbarMode;

    private OnBaseItemClickListener onPlaceItemClick = new OnBaseItemClickListener() {
        @Override
        public void onBaseItemClick(int position, View view) {
            MixedPlaceViewModel mixedPlaceViewModel =
                    (MixedPlaceViewModel) adapter.getItem(position);
            Marker marker = markersMap.get(mixedPlaceViewModel.getPlaceViewModel().getLatLng()).second;
            centerMapOnLatLng(marker.getPosition());
            marker.showInfoWindow();
            dismissClosestPlaces();

            //Tracking Event
            trackerManager.trackEvent(TrackerManager.EVENT_CLICK_PLACE_CLOSEST, "Place Name",
                    mixedPlaceViewModel.getPlaceViewModel().getName());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
        adapter.registerClickListeners(onPlaceItemClick, onTransparentItemClick);
        passarolaTabLayoutManager.registerListener(this);
        placeToolbarManager.registerListener(this);
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
        passarolaTabLayoutManager.unregisterListener();
        placeToolbarManager.unregisterListener();
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
        resetToolbarTitle();
    }

    private void resetToolbarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.passarola_brewing);
        }
    }

    private void setToolbarTitle(int stringResource) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(stringResource);
        }
    }

    private void setupTabs() {
        passarolaTabLayoutManager = new PassarolaTabLayoutManager(tabLayout);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case PassarolaTabLayoutManager.TAB_CLOSEST_POSITION:
                onClosestTabClick();
                break;

            case PassarolaTabLayoutManager.TAB_PLACES_POSITION:
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

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_TAB_CLOSEST);
    }

    private void onPlacesTabClick() {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_TAB_PLACES);
    }

    private void onBeersTabClick() {
        Intent intent = new Intent(this, BeersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_TAB_BEERS);
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
        placeToolbarManager = new PlaceToolbarManager(this);
    }

    @Override
    public void onFacebookClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_PLACE_FACEBOOK, "Facebook Url", link);
    }

    @Override
    public void onZomatoClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_PLACE_ZOMATO, "Zomato Url", link);
    }

    @Override
    public void onTripadvisorClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_PLACE_TRIPADVISOR, "Tripadvisor Url", link);
    }

    @Override
    public void onPhoneClick(String phone) {
        Utils.openContact(this, phone);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_PLACE_PHONE, "Phone", phone);
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
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnInfoWindowCloseListener(this);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                presenter.getPlaces();
            }
        });
        googleMap.setInfoWindowAdapter(new PlacesInfoWindowAdapter(getLayoutInflater()));
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
                markersMap.put(placeViewModel.getLatLng(), new Pair<>(placeViewModel, marker));
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
        passarolaTabLayoutManager.show(!show);

        if (show) {
            isClosestPlaceMode = true;
            recyclerView.setTranslationY(recyclerViewHeight);
            if (mapView != null) {
                AnimatorManager.fadeOutPartial(mapView);
            }
            AnimatorManager.slideInView(recyclerView, 0);
        } else {
            isClosestPlaceMode = false;
            if (mapView != null) {
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
    private PlaceViewModel getPlaceViewModelFromMap(Marker marker) {
        for (Map.Entry<LatLng, Pair<PlaceViewModel, Marker>> entry : markersMap.entrySet()) {
            if (entry.getValue().second.getPosition().equals(marker.getPosition())) {
                return entry.getValue().first;
            }
        }

        return null;
    }

    /**
     * Animate info window
     * See: https://github.com/googlemaps/android-samples/blob/master/ApiDemos/app/src/main/java/
     * com/example/mapdemo/MarkerDemoActivity.java
     *
     * @param marker marker clicked
     * @return false because we don't want to override the default behaviour
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setInfoWindowAnchor(0.5f, 1.0f * -t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_MARKER, "Marker Title", marker.getTitle());

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        showPlaceToolbar(marker);
        trackerManager.trackEvent("");

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_INFO_WINDOW, "Marker Title", marker.getTitle());
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        dismissPlaceToolbar();
    }

    private void showPlaceToolbar(Marker marker) {
        PlaceViewModel placeViewModel = getPlaceViewModelFromMap(marker);
        placeToolbarManager.show(placeToolbarLayout, placeViewModel);
        passarolaTabLayoutManager.show(false);
        isPlaceToolbarMode = true;
    }

    private void dismissPlaceToolbar() {
        placeToolbarManager.hide();
        passarolaTabLayoutManager.show(true);
        isPlaceToolbarMode = false;
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
        if (isClosestPlaceMode) {
            dismissClosestPlaces();
        } else if (isPlaceToolbarMode) {
            dismissPlaceToolbar();
        } else {
            super.onBackPressed();
        }
    }
}
