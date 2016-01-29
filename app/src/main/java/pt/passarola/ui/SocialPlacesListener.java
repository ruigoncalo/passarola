package pt.passarola.ui;

/**
 * Created by ruigoncalo on 28/01/16.
 */
public interface SocialPlacesListener {
    void onFacebookClick(String link);

    void onZomatoClick(String link);

    void onTripadvisorClick(String link);

    void onPhoneClick(String phone);
}
