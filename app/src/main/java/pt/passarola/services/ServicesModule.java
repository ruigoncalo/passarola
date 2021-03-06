package pt.passarola.services;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pt.passarola.services.rest.RestApi;
import pt.passarola.services.tracker.TrackerManager;

/**
 * Created by ruigoncalo on 22/10/15.
 */
@Module(library = true, complete = false)
public class ServicesModule {

    @Provides
    @Singleton
    BusProvider provideBus() {
        return new BusProvider();
    }

    @Provides
    @Singleton
    WebApiService provideWebApiService(RestApi restApi) {
        return new WebApiService(restApi);
    }

    @Provides
    @Singleton
    PlaceProvider providePlaceProvider(BusProvider busProvider, WebApiService webApiService) {
        return new PlaceProvider(busProvider, webApiService);
    }

    @Provides
    @Singleton
    LocationProvider providesLocationProvider(BusProvider busProvider, Context context){
        return new LocationProvider(busProvider, context);
    }

    @Provides
    @Singleton
    BeerProvider providesBeerProvider(BusProvider busProvider, WebApiService webApiService){
        return new BeerProvider(busProvider, webApiService);
    }

    @Provides
    @Singleton
    TrackerManager providesTrackerManager(){
        return new TrackerManager();
    }
}

