package pt.passarola.model.viewmodel;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class MixedPlaceViewModel implements MixedViewModel {

    private final PlaceViewModel placeViewModel;
    private final int type;

    public MixedPlaceViewModel(PlaceViewModel placeViewModel, int type) {
        this.placeViewModel = placeViewModel;
        this.type = type;
    }

    public PlaceViewModel getPlaceViewModel() {
        return placeViewModel;
    }

    @Override
    public int getType() {
        return type;
    }
}
