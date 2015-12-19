package pt.passarola;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pt.passarola.services.ServicesModule;
import pt.passarola.ui.UiModule;

/**
 * Created by ruigoncalo on 22/10/15.
 */
@Module(injects = App.class, includes = {UiModule.class, ServicesModule.class})
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context providesContext(){
        return app;
    }
}