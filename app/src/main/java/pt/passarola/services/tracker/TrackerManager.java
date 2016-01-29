package pt.passarola.services.tracker;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruigoncalo on 14/01/16.
 */
public class TrackerManager {

    public void trackEvent(String eventName){
        Answers.getInstance().logCustom(new CustomEvent(eventName));
    }

    public void trackEvent(String eventName, HashMap<String, String> attributes){
        CustomEvent customEvent = new CustomEvent(eventName);

        for(Map.Entry<String, String> entry : attributes.entrySet()){
            customEvent.putCustomAttribute(entry.getKey(), entry.getValue());
        }

        Answers.getInstance().logCustom(customEvent);
    }
}
