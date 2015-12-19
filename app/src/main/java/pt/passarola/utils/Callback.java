package pt.passarola.utils;

import android.location.Location;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public interface Callback<T> {
    void onLocationSuccessEvent(Location location);
    void onLocationErrorEvent(Exception e);
    void onPlacesSuccessEvent(T t);
    void onPlacesErrorEvent(Exception e);
    void isLoading(boolean loading);
}