package pt.passarola.model.events;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class ClosestPlacesErrorEvent extends PlacesErrorEvent {

    public ClosestPlacesErrorEvent(Exception e) {
        super(e);
    }
}
