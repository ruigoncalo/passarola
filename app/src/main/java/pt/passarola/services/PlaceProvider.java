package pt.passarola.services;

import android.location.Location;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import pt.passarola.model.ClosestPlace;
import pt.passarola.model.MetaPlaces;
import pt.passarola.model.Place;
import pt.passarola.model.events.ClosestPlacesSuccessEvent;
import pt.passarola.model.events.PlacesErrorEvent;
import pt.passarola.model.events.PlacesSuccessEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class PlaceProvider {

    private final static String LOCATION_PROVIDER = "";

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


    private boolean isLoading(){
        return requestsCounter.get() > 0;
    }

    private Map<Place, Location> getLocations(List<Place> places){
        Map<Place, Location> locations = new HashMap<>();
        for(Place place : places) {
            if (place.isValid()) {
                locations.put(place, getLocationFromPlace(place));
            }
        }

        return locations;
    }

    private Location getLocationFromPlace(Place place){
        Location location = new Location(LOCATION_PROVIDER);
        location.setLatitude(Double.valueOf(place.getLat()));
        location.setLongitude(Double.valueOf(place.getLng()));

        return location;
    }

    private Map<Place, Integer> getLocationMapOrderedByDistance(Location target, List<Place> places){
        Map<Place, Integer> map = new LinkedHashMap<>();
        Map<Place, Location> locations = getLocations(places);

        for(Map.Entry<Place, Location> entry : locations.entrySet()){
            int distance = (int) target.distanceTo(entry.getValue());
            map.put(entry.getKey(), distance);
        }

        ValueComparator valueComparator =  new ValueComparator(map);
        Map<Place, Integer> locationsMap = new TreeMap<>(valueComparator);
        locationsMap.putAll(map);

        return locationsMap;
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

    public void getClosestPlaces(Location location){
        List<ClosestPlace> closestPlaces = new ArrayList<>();

        Map<Place, Integer> placesOrderedByLocation =
                getLocationMapOrderedByDistance(location, places);

        int count = 0, max = 20;
        for(Map.Entry<Place, Integer> entry : placesOrderedByLocation.entrySet()){
            if(count >= max){
                break;
            }

            count++;
            closestPlaces.add(new ClosestPlace(entry.getKey(), entry.getValue()));
        }

        busProvider.post(new ClosestPlacesSuccessEvent(closestPlaces));
    }

    private class ValueComparator implements Comparator<Place> {

        Map<Place, Integer> base;
        public ValueComparator(Map<Place, Integer> base) {
            this.base = base;
        }

        public int compare(Place a, Place b) {
            if (base.get(a) <= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
