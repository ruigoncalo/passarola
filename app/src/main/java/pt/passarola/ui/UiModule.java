package pt.passarola.ui;

import dagger.Module;

/**
 * Created by ruigoncalo on 22/10/15.
 */
@Module(injects = {
        MainActivity.class,
        MapsFragment.class,
        AboutActivity.class,
        BeersFragment.class,
        BeerFragment.class,
        EventsFragment.class,
        PlacesFragment.class
},
        complete = false)
public class UiModule {

}

