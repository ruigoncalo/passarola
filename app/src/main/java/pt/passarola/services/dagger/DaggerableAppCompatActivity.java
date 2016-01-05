package pt.passarola.services.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pt.passarola.App;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public abstract class DaggerableAppCompatActivity extends AppCompatActivity implements Daggerable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(this);
    }

    @Override
    public void inject(Object object) {
        App.obtain(this).inject(object);
    }

    @Override
    public void inject(Object object, Object... modules) {
        App.obtain(this).inject(object, modules);
    }

}

