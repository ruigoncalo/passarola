package pt.passarola.services;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BusProvider extends Bus {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void post(final Object event) {
        if(event != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.super.post(event);
                    }
                });
            }
        }
    }
}
