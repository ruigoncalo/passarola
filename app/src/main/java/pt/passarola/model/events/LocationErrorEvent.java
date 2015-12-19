package pt.passarola.model.events;

/**
 * Created by ruigoncalo on 19/12/15.
 */
public class LocationErrorEvent extends ErrorEvent {

    public LocationErrorEvent(Exception e){
        super(e);
    }
}
