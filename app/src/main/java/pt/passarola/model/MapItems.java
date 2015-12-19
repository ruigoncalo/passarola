package pt.passarola.model;

import java.util.List;

import pt.passarola.model.viewmodel.PlaceViewModel;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class MapItems {

    private final List<PlaceViewModel> places;

    public MapItems(List<PlaceViewModel> places) {
        this.places = places;
    }

    public List<PlaceViewModel> getPlaces() {
        return places;
    }
}
