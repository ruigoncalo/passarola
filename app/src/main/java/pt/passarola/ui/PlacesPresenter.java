package pt.passarola.ui;

import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.MetaPlaces;
import pt.passarola.model.Place;
import pt.passarola.model.events.PlaceViewModelEvent;
import pt.passarola.model.viewmodel.PlaceViewModel;
import pt.passarola.model.events.ErrorEvent;
import pt.passarola.model.events.PlacesSuccessEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.services.WebApiService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        webApiService.getPlaces(new Callback<MetaPlaces>() {
            @Override
            public void success(MetaPlaces metaPlaces, Response response) {
                List<Place> places = metaPlaces.getData();
                List<PlaceViewModel> placeViewModelList = generateViewModelList(places);
                busProvider.post(new PlaceViewModelEvent(placeViewModelList));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Subscribe
    public void onPlacesEvent(PlacesSuccessEvent event){

    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event){

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
                    .build();

        }

        return result;
    }
}
