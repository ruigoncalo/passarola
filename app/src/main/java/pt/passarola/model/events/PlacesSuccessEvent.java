package pt.passarola.model.events;

import java.util.List;

import pt.passarola.model.Place;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class PlacesSuccessEvent {

    private final List<Place> placeList;

    public PlacesSuccessEvent(List<Place> placeList) {
        this.placeList = placeList;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }
}