package pt.passarola.model.events;

import java.util.List;

import pt.passarola.ui.viewmodel.PlaceViewModel;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlaceViewModelEvent {

    private final List<PlaceViewModel> placeViewModelList;

    public PlaceViewModelEvent(List<PlaceViewModel> placeViewModelList) {
        this.placeViewModelList = placeViewModelList;
    }

    public List<PlaceViewModel> getPlaceViewModelList() {
        return placeViewModelList;
    }
}
