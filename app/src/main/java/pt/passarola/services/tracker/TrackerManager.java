package pt.passarola.services.tracker;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruigoncalo on 14/01/16.
 */
public class TrackerManager {

    public static final String EVENT_CLICK_TAB_BEERS = "event-click-tab-beers";
    public static final String EVENT_CLICK_TAB_PLACES = "event-click-tab-places";
    public static final String EVENT_CLICK_TAB_CLOSEST = "event-click-tab-closest";
    public static final String EVENT_CLICK_PLACE_CLOSEST = "event-click-place-close";
    public static final String EVENT_CLICK_MARKER = "event-click-marker";
    public static final String EVENT_CLICK_INFO_WINDOW = "event-click-info-window";
    public static final String EVENT_CLICK_PLACE_PHONE = "event-click-place-phone";
    public static final String EVENT_CLICK_PLACE_FACEBOOK = "event-click-place-facebook";
    public static final String EVENT_CLICK_PLACE_ZOMATO = "event-click-place-zomato";
    public static final String EVENT_CLICK_PLACE_TRIPADVISOR = "event-click-tripadvisor";
    public static final String EVENT_CLICK_ALL_PLACES_FACEBOOK = "event-click-all-places-facebook";
    public static final String EVENT_CLICK_ALL_PLACES_ZOMATO = "event-click-all-places-zomato";
    public static final String EVENT_CLICK_ALL_PLACES_TRIPADVISOR = "event-click-all-places-tripadvisor";
    public static final String EVENT_CLICK_BEER_RATEBEER = "event-click-beer-ratebeer";
    public static final String EVENT_CLICK_BEER_UNTAPPD = "event-click-beer-untappd";
    public static final String EVENT_CLICK_ABOUT_EMAIL = "event-click-about-email";
    public static final String EVENT_CLICK_ABOUT_FACEBOOK = "event-click-about-facebook";

    public void trackEvent(String eventName){
        Answers.getInstance().logCustom(new CustomEvent(eventName));
    }

    public void trackEvent(String eventName, String key, String value){
        CustomEvent customEvent = new CustomEvent(eventName);
        customEvent.putCustomAttribute(key, value);

        Answers.getInstance().logCustom(customEvent);
    }

    public void trackEvent(String eventName, HashMap<String, String> attributes){
        CustomEvent customEvent = new CustomEvent(eventName);
        for(Map.Entry<String, String> entry : attributes.entrySet()){
            customEvent.putCustomAttribute(entry.getKey(), entry.getValue());
        }

        Answers.getInstance().logCustom(customEvent);
    }
}
