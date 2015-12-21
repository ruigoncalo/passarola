package pt.passarola.ui;

import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.events.BeersErrorEvent;
import pt.passarola.model.events.BeersSuccessEvent;
import pt.passarola.model.viewmodel.BeerViewModel;
import pt.passarola.services.BeerProvider;
import pt.passarola.services.BusProvider;
import pt.passarola.utils.Presenter;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersPresenter extends Presenter<BeersPresenterCallback> {

    @Inject BeerProvider beerProvider;
    @Inject BusProvider busProvider;

    @Inject
    public BeersPresenter(){

    }

    public void onStart(BeersPresenterCallback presented) {
        super.onStart(presented);
        busProvider.register(this);
    }

    public void onStop() {
        busProvider.unregister(this);
        super.onStop();
    }

    public void getBeers(){
        if(getPresented() != null){
            getPresented().isLoading(true);
        }

        beerProvider.getBeers();
    }

    @Subscribe
    public void onBeersSuccessEvent(BeersSuccessEvent event){
        if(getPresented() != null) {
            List<BeerViewModel> beerViewModels = BeerViewModel.createViewModelList(event.getBeerList());
            getPresented().onBeersSuccessEvent(beerViewModels);
            getPresented().isLoading(false);
        }
    }

    @Subscribe
    public void onBeersErrorEvent(BeersErrorEvent event){
        if(getPresented() != null){
            getPresented().onBeersErrorEvent(event.getException());
            getPresented().isLoading(false);
        }
    }

}
