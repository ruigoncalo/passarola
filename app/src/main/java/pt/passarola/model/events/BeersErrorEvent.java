package pt.passarola.model.events;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class BeersErrorEvent extends ErrorEvent {

    public BeersErrorEvent(Exception e) {
        super(e);
    }
}
