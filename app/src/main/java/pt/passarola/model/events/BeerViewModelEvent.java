package pt.passarola.model.events;

import java.util.List;

import pt.passarola.ui.viewmodel.BeerViewModel;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeerViewModelEvent {

    private final List<BeerViewModel> beerViewModelList;

    public BeerViewModelEvent(List<BeerViewModel> beerViewModelList) {
        this.beerViewModelList = beerViewModelList;
    }

    public List<BeerViewModel> getBeerViewModelList() {
        return beerViewModelList;
    }
}
