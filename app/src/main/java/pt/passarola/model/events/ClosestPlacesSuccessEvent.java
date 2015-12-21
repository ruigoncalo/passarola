package pt.passarola.model.events;

import java.util.List;

import pt.passarola.model.ClosestPlace;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class ClosestPlacesSuccessEvent {

    private List<ClosestPlace> closestPlaces;

    public ClosestPlacesSuccessEvent(List<ClosestPlace> closestPlaces){
        this.closestPlaces = closestPlaces;
    }

    public List<ClosestPlace> getClosestPlaces() {
        return closestPlaces;
    }
}
