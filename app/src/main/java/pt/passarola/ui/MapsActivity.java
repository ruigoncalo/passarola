package pt.passarola.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.MapItems;
import pt.passarola.model.viewmodel.MixedPlaceViewModel;
import pt.passarola.model.viewmodel.MixedViewModel;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.ui.recyclerview.OnBaseItemClickListener;
import pt.passarola.ui.recyclerview.PlacesMixedAdapter;
import pt.passarola.utils.Utils;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class MapsActivity extends DaggerableAppCompatActivity
        implements OnMapReadyCallback, MapPresenterCallback {

    private static final String BUNDLE_KEY_MAP_STATE = "bundle-map-state";
    private static final String TAB_CLOSEST = "tab-closest";
    private static final String TAB_PLACES = "tab-places";
    private static final String TAB_BEERS = "tab-beers";
    private static final int TAB_CLOSEST_POSITION = 0;
    private static final int TAB_PLACES_POSITION = 1;
    private static final int TAB_BEERS_POSITION = 2;

    @Inject MapsPresenter presenter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.map_view) MapView mapView;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.tabs_layout) TabLayout tabLayout;
    @Bind(R.id.layout_info) View infoView;
    @Bind(R.id.text_info) TextView infoText;

    private Location currentLocation;
    private GoogleMap googleMap;
    private PlacesMixedAdapter adapter;
    private float tabHeightInPx;

    private OnBaseItemClickListener onPlaceItemClick = new OnBaseItemClickListener() {
        @Override
        public void onBaseItemClick(int position, View view) {
            MixedPlaceViewModel mixedPlaceViewModel =
                    (MixedPlaceViewModel) adapter.getItem(position);
            centerMapOnLatLng(mixedPlaceViewModel.getPlaceViewModel().getLatLng());
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
        setupToolbar();
        setupTabs();
        setupMap(savedInstanceState);
        setupRecyclerView();
        setupInfoView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
        adapter.registerClickListeners(onPlaceItemClick, onTransparentItemClick);
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
        adapter.unregisterClickListeners();
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
        TabLayout.Tab closestPlacesTab = tabLayout.newTab();
        closestPlacesTab.setTag(TAB_CLOSEST);
        closestPlacesTab.setIcon(R.drawable.ic_closest);

        TabLayout.Tab allPlacesTab = tabLayout.newTab();
        allPlacesTab.setTag(TAB_PLACES);
        allPlacesTab.setIcon(R.drawable.ic_action_action_view_list);

        TabLayout.Tab beersTab = tabLayout.newTab();
        beersTab.setTag(TAB_BEERS);
        beersTab.setIcon(R.drawable.ic_beers);

        tabLayout.addTab(closestPlacesTab, TAB_CLOSEST_POSITION);
        tabLayout.addTab(allPlacesTab, TAB_PLACES_POSITION);
        tabLayout.addTab(beersTab, TAB_BEERS_POSITION);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_CLOSEST_POSITION:
                        onClosestTabClick();
                        break;

                    case TAB_PLACES_POSITION:
                        onPlacesTabClick();
                        break;

                    default:
                        onBeersClick();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }

    private void setupInfoView(){
        infoView.setVisibility(View.GONE);
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

    private void onBeersClick() {
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
        mapView.onCreate(mapState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupGoogleMap();
        presenter.getCurrentLocation();
    }

    private void setupGoogleMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void showLocationOnMap(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                presenter.getPlaces();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void showMarkers(List<PlaceViewModel> places) {
        if(googleMap != null) {
            for (PlaceViewModel placeViewModel : places) {
                googleMap.addMarker(createMarkerOption(placeViewModel));
            }
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
        showTabs(!show);
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

    private void showTabs(boolean show) {
        if(tabHeightInPx == 0){
            int tabHeightInDp = tabLayout.getHeight();
            if(tabHeightInDp == 0){
                tabHeightInPx = 400;
            } else {
                tabHeightInPx = Utils.dpToPx(this, tabHeightInDp);
            }
        }

        if (show) {
            tabLayout.animate()
                    .translationY(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200);
        } else {
            tabLayout.animate()
                    .translationY(tabHeightInPx)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200);
        }
    }

    private void centerMapOnLatLng(LatLng latlng) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
        }
    }

    private void showInfoView(boolean show, String message){
        if(show){
            infoView.setVisibility(View.VISIBLE);
            infoText.setText(message);
        } else {
            infoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLocationSuccessEvent(Location location) {
        currentLocation = location;
        showLocationOnMap(location);
        showInfoView(false, null);
    }

    @Override
    public void onLocationErrorEvent(Exception e) {
        showInfoView(true, "We could not get your current location. " +
                "Please check your mobile phone settings and turn on " +
                "your location service.");
    }

    @Override
    public void onPlacesSuccessEvent(MapItems mapItems) {
        showMarkers(mapItems.getPlaces());
    }

    @Override
    public void onPlacesErrorEvent(Exception e) {

    }

    @Override
    public void onClosestPlacesSuccessEvent(List<MixedViewModel> list) {
        loadClosestPlaces(list);
    }

    @Override
    public void onClosestPlacesErrorEvent(Exception e) {

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
