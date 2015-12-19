package pt.passarola.model.events;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class ErrorEvent {

    private final Exception e;

    public ErrorEvent(Exception e) {
        this.e = e;
    }

    public Exception getException() {
        return e;
    }
}