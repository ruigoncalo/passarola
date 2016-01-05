package pt.passarola.utils;

import android.content.Context;

import com.google.android.gms.common.GoogleApiAvailability;

import static com.google.android.gms.common.ConnectionResult.SUCCESS;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class Utils {

    /**
     * Validate if device has GooglePlayServices app installed
     */
    public static boolean isGooglePlayServicesAvailable(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == SUCCESS;
    }
}