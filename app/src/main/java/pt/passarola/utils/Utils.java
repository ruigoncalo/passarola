package pt.passarola.utils;

import android.content.Context;
import android.util.TypedValue;

import com.google.android.gms.common.GooglePlayServicesUtil;

import static com.google.android.gms.common.ConnectionResult.SUCCESS;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class Utils {

    /**
     * Validate if user has GooglePlayServices app
     */
    public static boolean isGooglePlayServicesAvailable(Context context) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == SUCCESS;
    }

    /**
     * converting dp to px
     */
    public static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
