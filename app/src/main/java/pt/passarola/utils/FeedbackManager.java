package pt.passarola.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ruigoncalo on 27/12/15.
 */
public class FeedbackManager {

    public static void showFeedback(View parent, String message){
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showFeedbackIndeterminate(View parent, String message, String actionMessage,
                                                 View.OnClickListener clickListener){
        Snackbar.make(parent, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionMessage, clickListener)
                .show();
    }
}
