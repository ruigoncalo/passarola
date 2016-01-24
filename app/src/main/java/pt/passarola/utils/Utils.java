package pt.passarola.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.android.gms.common.GoogleApiAvailability;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * Normalize the value of a distance expressed in meters.
     * 989 meters = 989 meters
     * 1000 meters = 1.0 km
     * 1500 meters = 1.5 km
     *
     * @param distanceInMeters distance number in meters
     * @return distance normalized with distance units
     */
    public static String getNormalizedDistance(int distanceInMeters){
        float distance = (float) distanceInMeters;
        if(distance < 1000){
            return distanceInMeters + " meters";
        } else {
            return String.format("%.02f", distance/1000) + " km";
        }
    }

    /**
     * Generate a date object from a string
     *
     * @param date string date
     * @return date object that follows the format "yyyy-mm-dd"
     */
    @Nullable
    public static Date getDateFromString(String date){
        Date result;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.UK);

        try {
            result = dateFormat.parse(date);
        } catch (ParseException e){
            result = null;
        }

        return result;
    }

    /**
     * Open intent with data
     *
     * @param context used to start activity
     * @param link intent's data
     */
    public static void openLink(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }

    /**
     * Open contact screen by providing a phone number
     * @param context used to start activity
     * @param phoneNumber data to show on contact screen
     */
    public static void openContact(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

}