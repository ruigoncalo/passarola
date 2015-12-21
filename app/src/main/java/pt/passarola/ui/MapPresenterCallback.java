package pt.passarola.ui;

import android.location.Location;

import java.util.List;

import pt.passarola.model.MapItems;
import pt.passarola.model.viewmodel.MixedViewModel;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public interface MapPresenterCallback {
    void onLocationSuccessEvent(Location location);
    void onLocationErrorEvent(Exception e);
    void onPlacesSuccessEvent(MapItems items);
    void onPlacesErrorEvent(Exception e);
    void onClosestPlacesSuccessEvent(List<MixedViewModel> list);
    void onClosestPlacesErrorEvent(Exception e);
    void isLoading(boolean loading);
}