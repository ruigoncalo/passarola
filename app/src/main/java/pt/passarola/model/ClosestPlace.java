package pt.passarola.model;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class ClosestPlace {

    private final Place place;
    private final int distance;

    public ClosestPlace(Place place, int distance) {
        this.place = place;
        this.distance = distance;
    }

    public Place getPlace() {
        return place;
    }

    public int getDistance() {
        return distance;
    }
}
