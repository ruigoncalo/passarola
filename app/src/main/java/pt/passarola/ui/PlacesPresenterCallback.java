package pt.passarola.ui;

import pt.passarola.model.MapItems;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public interface PlacesPresenterCallback {
    void onPlacesSuccessEvent(MapItems items);
    void onPlacesErrorEvent(Exception e);
    void isLoading(boolean loading);
}
