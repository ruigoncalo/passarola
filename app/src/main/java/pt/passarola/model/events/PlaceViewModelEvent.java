package pt.passarola.model.events;

import pt.passarola.model.PlaceViewModel;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlaceViewModelEvent {

    private final PlaceViewModel placeViewModel;

    public PlaceViewModelEvent(PlaceViewModel placeViewModel) {
        this.placeViewModel = placeViewModel;
    }

    public PlaceViewModel getPlaceViewModel() {
        return placeViewModel;
    }
}
