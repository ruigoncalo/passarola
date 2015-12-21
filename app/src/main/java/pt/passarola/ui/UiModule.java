package pt.passarola.ui;

import dagger.Module;

/**
 * Created by ruigoncalo on 22/10/15.
 */
@Module(injects = {
        MapsActivity.class,
        AboutActivity.class,
        BeersActivity.class,
        PlacesActivity.class,
        SplashActivity.class
},
        complete = false)
public class UiModule {

}

