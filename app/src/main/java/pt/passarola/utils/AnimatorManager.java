package pt.passarola.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by ruigoncalo on 05/01/16.
 */
public class AnimatorManager {

    private static final int SHORT_DURATION = 200; //ms

    public static void fadeInPartial(@NonNull final View view){
        view.setAlpha(0.5f);
        view.animate()
                .alpha(1)
                .setDuration(SHORT_DURATION);
    }

    public static void fadeOutPartial(@NonNull final View view){
        view.animate()
                .alpha(0.5f)
                .setDuration(SHORT_DURATION);
    }

    public static void fadeInView(@NonNull final View view){
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .setDuration(200)
                .setListener(null);
    }

    public static void fadeOutView(@NonNull final View view){
        view.animate()
                .alpha(0f)
                .setDuration(SHORT_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }

    public static void slideInView(@NonNull final View view, int distance){
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(distance)
                .setDuration(SHORT_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(null);
    }

    public static void slideOutView(@NonNull final View view, int distance){
        view.animate()
                .translationY(distance)
                .setDuration(SHORT_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }
}
