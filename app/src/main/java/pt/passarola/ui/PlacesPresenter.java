package pt.passarola.ui;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.MapItems;
import pt.passarola.model.Place;
import pt.passarola.model.events.PlacesErrorEvent;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.model.events.PlacesSuccessEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.services.PlaceProvider;
import pt.passarola.utils.Presenter;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesPresenter extends Presenter<PlacesPresenterCallback> {

    @Inject PlaceProvider placeProvider;
    @Inject BusProvider busProvider;

    @Inject
    public PlacesPresenter(){

    }

    public void onStart(PlacesPresenterCallback presented) {
        super.onStart(presented);
        busProvider.register(this);
    }

    public void onStop() {
        busProvider.unregister(this);
        super.onStop();
    }

    public void getPlaces(){
        if(getPresented() != null){
            getPresented().isLoading(true);
        }

        placeProvider.getPlaces();
    }

    @Subscribe
    public void onPlacesSuccessEvent(PlacesSuccessEvent event){
        if(getPresented() != null) {
            final List<Place> validPlaces = new ArrayList<>(event.getPlaceList());
            final List<PlaceViewModel> placeViewModels = PlaceViewModel.createViewModelList(validPlaces);
            final MapItems mapItems = new MapItems(placeViewModels);
            getPresented().onPlacesSuccessEvent(mapItems);
            getPresented().isLoading(false);
        }
    }

    @Subscribe
    public void onPlacesErrorEvent(PlacesErrorEvent event){
        if(getPresented() != null){
            getPresented().onPlacesErrorEvent(event.getException());
            getPresented().isLoading(false);
        }
    }
}
