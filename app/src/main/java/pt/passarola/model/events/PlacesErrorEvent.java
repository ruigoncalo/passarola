package pt.passarola.model.events;

/**
 * Created by ruigoncalo on 18/12/15.
 */
public class PlacesErrorEvent extends ErrorEvent {

    public PlacesErrorEvent(Exception e){
        super(e);
    }
}
