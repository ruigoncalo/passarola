package pt.passarola.services;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pt.passarola.utils.rest.RestApi;

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
    @Singleton WebApiService provideWebApiService(RestApi restApi, BusProvider busProvider) {
        return new WebApiService(restApi, busProvider);
    }
}

