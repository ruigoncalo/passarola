package pt.passarola.ui;

import dagger.Module;

/**
 * Created by ruigoncalo on 22/10/15.
 */
@Module(injects = {
        MainActivity.class,
        MapsActivity.class,
        AboutActivity.class,
        BeersActivity.class,
        PlacesActivity.class
},
        complete = false)
public class UiModule {

}

