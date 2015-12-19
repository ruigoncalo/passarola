package pt.passarola.model.events;

import android.location.Location;

/**
 * Created by ruigoncalo on 19/12/15.
 */
public class LocationSuccessEvent {

    private final Location location;

    public LocationSuccessEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
