package pt.passarola.ui;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import pt.passarola.model.Place;
import pt.passarola.model.PlaceViewModel;
import pt.passarola.model.events.ErrorEvent;
import pt.passarola.model.events.PlaceViewModelEvent;
import pt.passarola.model.events.PlacesEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.services.WebApiService;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesPresenter {

    @Inject WebApiService webApiService;
    @Inject BusProvider busProvider;

    @Inject
    public PlacesPresenter(){

    }

    public void start(){
        busProvider.register(this);
    }

    public void stop(){
        busProvider.unregister(this);
    }

    public void getItems(){
        webApiService.getPlaces();
    }

    @Subscribe
    public void onPlacesEvent(PlacesEvent event){
        for(Place place : event.getPlaceList()){
            PlaceViewModel placeViewModel = createPlaceViewModel(place);
            busProvider.post(new PlaceViewModelEvent(placeViewModel));
        }
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event){
        Timber.d(event.getMessage());
    }

    // TODO: check fields
    private PlaceViewModel createPlaceViewModel(Place place){
        return new PlaceViewModel.Builder()
                .id(place.getId())
                .name(place.getName())
                .fullAddress(place.getFullAddress())
                .council(place.getCouncil())
                .country(place.getCountry())
                .telephone(place.getTelephone())
                .build();
    }
}
