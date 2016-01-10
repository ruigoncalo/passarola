package pt.passarola.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import pt.passarola.model.Beer;
import pt.passarola.model.MetaBeers;
import pt.passarola.model.events.BeersErrorEvent;
import pt.passarola.model.events.BeersSuccessEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeerProvider {

    private List<Beer> beers;
    private BusProvider busProvider;
    private WebApiService webApiService;
    private AtomicInteger requestsCounter;

    public BeerProvider(BusProvider busProvider, WebApiService webApiService) {
        this.busProvider = busProvider;
        this.webApiService = webApiService;
        this.beers = new ArrayList<>();
        this.requestsCounter = new AtomicInteger();
    }

    public void getBeers(){
        if(beers.isEmpty()){
            if(isLoading()) {
                busProvider.post(new BeersErrorEvent(new Exception("Request is not completed")));
            } else {
                fetchBeers();
            }
        } else {
            busProvider.post(new BeersSuccessEvent(beers));
        }
    }

    private void fetchBeers() {
        requestsCounter.incrementAndGet();
        webApiService.getBeers(new Callback<MetaBeers>() {
            @Override
            public void success(MetaBeers metaBeers, Response response) {
                requestsCounter.decrementAndGet();
                addBeers(metaBeers.getData());
                busProvider.post(new BeersSuccessEvent(beers));
            }

            @Override
            public void failure(RetrofitError error) {
                requestsCounter.decrementAndGet();
                busProvider.post(new BeersErrorEvent(error));
            }
        });
    }

    private void addBeers(List<Beer> beers){
        // order beers
        Set<Beer> beerSet = new TreeSet<>();
        for(Beer beer : beers){
            beerSet.add(beer);
        }

        for(Beer beer : beerSet){
            this.beers.add(beer);
        }
    }

    private boolean isLoading(){
        return requestsCounter.get() > 0;
    }
}
