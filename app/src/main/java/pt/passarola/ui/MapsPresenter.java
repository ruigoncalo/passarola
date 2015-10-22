package pt.passarola.ui;

import javax.inject.Inject;

import pt.passarola.services.BusProvider;
import pt.passarola.services.WebApiService;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class MapsPresenter {

    @Inject WebApiService webApiService;
    @Inject BusProvider busProvider;

    @Inject
    public MapsPresenter(){

    }

    public void getPlaces(){
        webApiService.getPlaces();
    }

}
