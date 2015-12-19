package pt.passarola.ui;

import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.MapItems;
import pt.passarola.model.Place;
import pt.passarola.model.events.PlacesErrorEvent;
import pt.passarola.model.events.PlacesSuccessEvent;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.services.BusProvider;
import pt.passarola.services.PlaceProvider;
import pt.passarola.utils.Callback;
import pt.passarola.utils.Presenter;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class MapsPresenter extends Presenter<Callback<MapItems>>{

    @Inject PlaceProvider placeProvider;
    @Inject BusProvider busProvider;

    public MapsPresenter(){

    }

    public void onStart(Callback<MapItems> presented){
        super.onStart(presented);
        busProvider.register(this);
    }

    public void onStop(){
        busProvider.unregister(this);
        super.onStop();
    }

    public void getPlaces(){
        if(getPresented() != null){
            getPresented().isLoading(true);
        }

        placeProvider.getPlaces();
    }

    private List<PlaceViewModel> generateViewModelList(List<Place> places){
        List<PlaceViewModel> result = new ArrayList<>();
        for(Place place : places){
            PlaceViewModel placeViewModel = createPlaceViewModel(place);
            if(placeViewModel != null){
                result.add(placeViewModel);
            }
        }

        return result;
    }

    @Nullable
    private PlaceViewModel createPlaceViewModel(Place place){
        PlaceViewModel result = null;
        if(place.isValid()){
            result = new PlaceViewModel.Builder()
                    .id(place.getId())
                    .name(place.getName())
                    .fullAddress(place.getFullAddress())
                    .council(place.getCouncil())
                    .country(place.getCountry())
                    .telephone(place.getTelephone())
                    .latLng(place.getLatLng())
                    .build();

        }

        return result;
    }

    @Subscribe
    public void onPlacesSuccessEvent(PlacesSuccessEvent event){
        if(getPresented() != null) {
            final List<Place> validPlaces = new ArrayList<>(event.getPlaceList());
            final List<PlaceViewModel> placeViewModels = generateViewModelList(validPlaces);
            final MapItems mapItems = new MapItems(placeViewModels);
            getPresented().onSuccess(mapItems);
            getPresented().isLoading(false);
        }
    }

    @Subscribe
    public void onPlacesErrorEvent(PlacesErrorEvent event){
        if(getPresented() != null){
            getPresented().onFailure(event.getException());
            getPresented().isLoading(false);
        }
    }
}
