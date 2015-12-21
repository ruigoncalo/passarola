package pt.passarola.ui;

import android.location.Location;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.ClosestPlace;
import pt.passarola.model.MapItems;
import pt.passarola.model.Place;
import pt.passarola.model.events.ClosestPlacesSuccessEvent;
import pt.passarola.model.events.LocationErrorEvent;
import pt.passarola.model.events.LocationSuccessEvent;
import pt.passarola.model.events.PlacesErrorEvent;
import pt.passarola.model.events.PlacesSuccessEvent;
import pt.passarola.model.viewmodel.MixedPlaceViewModel;
import pt.passarola.model.viewmodel.MixedViewModel;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.services.BusProvider;
import pt.passarola.services.LocationProvider;
import pt.passarola.services.PlaceProvider;
import pt.passarola.utils.Presenter;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class MapsPresenter extends Presenter<MapPresenterCallback> {

    @Inject LocationProvider locationProvider;
    @Inject PlaceProvider placeProvider;
    @Inject BusProvider busProvider;

    @Inject
    public MapsPresenter() {

    }

    public void onStart(MapPresenterCallback presented) {
        super.onStart(presented);
        busProvider.register(this);
    }

    public void onStop() {
        busProvider.unregister(this);
        super.onStop();
    }

    public void getCurrentLocation() {
        locationProvider.getCurrentLocation();
    }

    public void getPlaces() {
        if (getPresented() != null) {
            getPresented().isLoading(true);
        }

        placeProvider.getPlaces();
    }

    public void getClosestPlaces(Location location) {
        placeProvider.getClosestPlaces(location);
    }

    private List<MixedViewModel> generateMixedViewModelList(List<ClosestPlace> places) {
        List<MixedViewModel> result = new ArrayList<>();
        for (ClosestPlace closestPlace : places) {
            PlaceViewModel placeViewModel = PlaceViewModel.createPlaceViewModel(closestPlace);
            if (placeViewModel != null) {
                MixedPlaceViewModel mixedPlaceViewModel =
                        new MixedPlaceViewModel(placeViewModel, MixedViewModel.TYPE_A);
                result.add(mixedPlaceViewModel);
            }
        }

        return result;
    }

    @Subscribe
    public void onLocationSuccessEvent(LocationSuccessEvent event) {
        if (getPresented() != null) {
            getPresented().onLocationSuccessEvent(event.getLocation());
        }
    }

    @Subscribe
    public void onLocationErrorEvent(LocationErrorEvent event) {
        if (getPresented() != null) {
            getPresented().onLocationErrorEvent(event.getException());
        }
    }

    @Subscribe
    public void onPlacesSuccessEvent(PlacesSuccessEvent event) {
        if (getPresented() != null) {
            final List<Place> places = new ArrayList<>(event.getPlaceList());
            final List<PlaceViewModel> placeViewModels = PlaceViewModel.createViewModelList(places);
            final MapItems mapItems = new MapItems(placeViewModels);
            getPresented().onPlacesSuccessEvent(mapItems);
            getPresented().isLoading(false);
        }
    }

    @Subscribe
    public void onPlacesErrorEvent(PlacesErrorEvent event) {
        if (getPresented() != null) {
            getPresented().onPlacesErrorEvent(event.getException());
            getPresented().isLoading(false);
        }
    }

    @Subscribe
    public void onClosestPlacesSuccessEvent(ClosestPlacesSuccessEvent event) {
        if (getPresented() != null) {
            final List<ClosestPlace> places = new ArrayList<>(event.getClosestPlaces());
            final List<MixedViewModel> mixedPlaceViewModels = generateMixedViewModelList(places);

            // add first item as transparent
            final PlaceViewModel transparentItem = new PlaceViewModel.Builder().build();
            final MixedViewModel transparentViewModel =
                    new MixedPlaceViewModel(transparentItem, MixedViewModel.TYPE_B);

            mixedPlaceViewModels.add(0, transparentViewModel);
            getPresented().onClosestPlacesSuccessEvent(mixedPlaceViewModels);
        }
    }
}
