package pt.passarola.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pt.passarola.model.MetaPlaces;
import pt.passarola.model.Place;
import pt.passarola.model.events.PlacesErrorEvent;
import pt.passarola.model.events.PlacesSuccessEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class PlaceProvider {

    private List<Place> places;
    private WebApiService webApiService;
    private BusProvider busProvider;
    private AtomicInteger requestsCounter;

    public PlaceProvider(BusProvider busProvider, WebApiService webApiService) {
        this.busProvider = busProvider;
        this.webApiService = webApiService;
        places = new ArrayList<>();
        requestsCounter = new AtomicInteger();
    }

    private void fetchPlaces() {
        requestsCounter.incrementAndGet();
        webApiService.getPlaces(new Callback<MetaPlaces>() {
            @Override
            public void success(MetaPlaces metaPlaces, Response response) {
                requestsCounter.decrementAndGet();
                addPlaces(metaPlaces.getData());
                busProvider.post(new PlacesSuccessEvent(places));
            }

            @Override
            public void failure(RetrofitError error) {
                requestsCounter.decrementAndGet();
                busProvider.post(new PlacesErrorEvent(error));
            }
        });
    }

    private void addPlaces(List<Place> places){
        this.places.addAll(places);
    }

    public void getPlaces(){
        if(places.isEmpty()){
            if(isLoading()) {
                busProvider.post(new PlacesErrorEvent(new Exception("Request is not completed")));
            } else {
                fetchPlaces();
            }
        } else {
            busProvider.post(new PlacesSuccessEvent(places));
        }
    }

    private boolean isLoading(){
        return requestsCounter.get() > 0;
    }
}
