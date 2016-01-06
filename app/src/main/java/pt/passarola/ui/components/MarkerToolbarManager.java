package pt.passarola.ui.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.passarola.R;

/**
 * Created by ruigoncalo on 04/01/16.
 */
public class MarkerToolbarManager {

    public static final int ICON_1 = 1;
    public static final int ICON_2 = 2;
    public static final int ICON_3 = 3;

    private Context context;
    private View markerToolbar;
    private OnMarkerToolbarClickListener listener;

    public MarkerToolbarManager(Context context){
        this.context = context;
    }

    public void registerListener(OnMarkerToolbarClickListener listener){
        this.listener = listener;
    }

    public void unregisterListener(){
        this.listener = null;
    }

    public void show(ViewGroup anchor){
        View markerToolbar = LayoutInflater.from(context).inflate(R.layout.marker_toolbar, anchor, true);
        if(markerToolbar != null){
            this.markerToolbar = markerToolbar;

            View icon1 = markerToolbar.findViewById(R.id.icon_1);
            if(icon1 != null){
                setClickListener(ICON_1, icon1);
            }

            View icon2 = markerToolbar.findViewById(R.id.icon_2);
            if(icon2 != null){
                setClickListener(ICON_2, icon2);
            }

            View icon3 = markerToolbar.findViewById(R.id.icon_3);
            if(icon3 != null){
                setClickListener(ICON_3, icon3);
            }

            // show animation
            markerToolbar.setAlpha(0f);
            markerToolbar.setVisibility(View.VISIBLE);
            markerToolbar.animate()
                    .alpha(1)
                    .setDuration(200)
                    .setListener(null);
        }
    }

    private void setClickListener(final int position, View icon){
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onMarkerToolbarIconClick(position);
                }
            }
        });
    }

    public void hide(){
        if(markerToolbar != null){
            markerToolbar.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            markerToolbar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    public interface OnMarkerToolbarClickListener {
        void onMarkerToolbarIconClick(int position);
    }

}
