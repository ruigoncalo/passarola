package pt.passarola.model.events;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class ErrorEvent {

    private final String message;

    public ErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}