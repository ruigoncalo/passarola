package pt.passarola.services.dagger;

/**
 * Created by ruigoncalo on 31/10/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;

import pt.passarola.App;

public class DaggerableFragment extends Fragment implements Daggerable {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inject(this);
    }

    @Override
    public void inject(Object object) {
        App.obtain(getActivity()).inject(object);
    }

    @Override public void inject(Object object, Object... modules) {
        App.obtain(getActivity()).inject(object, modules);
    }
}
