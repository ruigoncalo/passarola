package pt.passarola.model.events;

import java.util.List;

import pt.passarola.model.Beer;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class BeersSuccessEvent {

    private final List<Beer> beerList;

    public BeersSuccessEvent(List<Beer> beerList) {
        this.beerList = beerList;
    }

    public List<Beer> getBeerList() {
        return beerList;
    }
}
