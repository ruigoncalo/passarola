package pt.passarola.ui;

import java.util.List;

import pt.passarola.model.viewmodel.BeerViewModel;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public interface BeersPresenterCallback {
    void onBeersSuccessEvent(List<BeerViewModel> list);
    void onBeersErrorEvent(Exception e);
    void isLoading(boolean loading);
}
