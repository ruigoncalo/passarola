package pt.passarola;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import pt.passarola.services.dagger.Daggerable;
import pt.passarola.services.rest.RestApi;
import timber.log.Timber;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class App extends Application implements Daggerable {

    private ObjectGraph objectGraph;

    public static App obtain(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        buildDebugOptions();
        buildObjectGraph();
    }

    @Override
    public void inject(Object object) {
        objectGraph.inject(object);
    }

    @Override
    public void inject(Object object, Object... modules) {
        objectGraph.plus(modules).inject(object);
    }

    private void buildObjectGraph() {
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AppModule(this));
    }

    private void buildDebugOptions() {
        if (BuildConfig.DEBUG) {
            RestApi.DEBUG = true;
            Timber.plant(new Timber.DebugTree());
        }
    }
}
