package pt.passarola.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import javax.inject.Inject;

import pt.passarola.services.PlaceProvider;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Based on https://www.bignerdranch.com/blog/splash-screens-the-right-way/
 *
 * Created by ruigoncalo on 18/12/15.
 */
public class SplashActivity extends DaggerableAppCompatActivity {

    private static final int LIFETIME_MS = 2000;

    @Inject PlaceProvider placeProvider;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, LIFETIME_MS);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    private void initTasks(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                goToMainActivity();
            }
        };

        placeProvider.getPlaces();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
