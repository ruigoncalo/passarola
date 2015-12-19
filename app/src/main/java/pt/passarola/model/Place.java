package pt.passarola.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class Place {

    private String id;
    private String name;
    @SerializedName("full_address")
    private String fullAddress;
    private String location;
    private String council;
    private String country;
    private String lat;
    private String lng;
    private String type;
    private String facebook;
    private String gmaps;
    private String tripadvisor;
    private String website;
    private String email;
    private String telephone;
    private String active;
    private String updated;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getLocation() {
        return location;
    }

    public String getCouncil() {
        return council;
    }

    public String getCountry() {
        return country;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getType() {
        return type;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGmaps() {
        return gmaps;
    }

    public String getTripadvisor() {
        return tripadvisor;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getActive() {
        return active;
    }

    public String getUpdated() {
        return updated;
    }

    public LatLng getLatLng(){
        Double dLat = Double.valueOf(lat);
        Double dLng = Double.valueOf(lng);
        return new LatLng(dLat, dLng);
    }

    public boolean isValid(){
        return name != null && !name.isEmpty() &&
                lat != null && !lat.isEmpty() &&
                lng != null && !lng.isEmpty();
    }
}

