package pt.passarola.ui;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.Place;
import pt.passarola.model.events.ErrorEvent;
import pt.passarola.model.events.PlacesEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.services.WebApiService;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class MapsPresenter {

    @Inject WebApiService webApiService;
    @Inject BusProvider busProvider;

    @Inject
    public MapsPresenter(){

    }

    public void start(){
        busProvider.register(this);
    }

    public void stop(){
        busProvider.unregister(this);
    }

    public void getPlaces(){
        webApiService.getPlaces();
    }

    @Subscribe
    public void onPlacesEvent(PlacesEvent event){
        List<Place> result = new ArrayList<>(event.getPlaceList().size());
        for(Place place : event.getPlaceList()){
            if(place.isValid()){
                result.add(place);
            }
        }

        busProvider.post(result);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event){
        Timber.d(event.getMessage());
    }


}
