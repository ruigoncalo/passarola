package pt.passarola.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


/**
 * Created by ruigoncalo on 04/01/16.
 */
public class ScreenInspector {

    private static final int STATUS_BAR_HEIGHT_DP = 24;
    private static final int APP_BAR_HEIGHT_DP = 56;

    public static double getDensity(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static int getScreenWidthPx(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getScreenHeightPx(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidthDp(Context context){
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.screenWidthDp;
    }

    public int getScreenHeightDp(Context context){
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.screenHeightDp;
    }

    public int getStatusBarHeightPx(Context context){
        return (int) dpToPx(context, STATUS_BAR_HEIGHT_DP);
    }

    public static int getAppBarHeightPx(Context context){
        return (int) dpToPx(context, APP_BAR_HEIGHT_DP);
    }

    /**
     * Get Navigation Bar (bottom) height in px
     */
    public static int getNavigationBarHeightPx(Context context){
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return (resourceId > 0) ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    public static float pxToDp(Context context, int px){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float dpToPx(Context context, int dp){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
