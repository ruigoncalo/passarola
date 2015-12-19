package pt.passarola.model.viewmodel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlaceViewModel {

    private final String id;
    private final String name;
    private final String fullAddress;
    private final String council;
    private final String country;
    private final String telephone;
    private final LatLng latLng;

    private PlaceViewModel(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.fullAddress = builder.fullAddress;
        this.council = builder.council;
        this.country = builder.country;
        this.telephone = builder.telephone;
        this.latLng = builder.latLng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getCouncil() {
        return council;
    }

    public String getCountry() {
        return country;
    }

    public String getTelephone() {
        return telephone;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public static class Builder {
        private String id;
        private String name;
        private String fullAddress;
        private String council;
        private String country;
        private String telephone;
        private LatLng latLng;

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder fullAddress(String fullAddress){
            this.fullAddress = fullAddress;
            return this;
        }

        public Builder council(String council){
            this.council = council;
            return this;
        }

        public Builder country(String country){
            this.country = country;
            return this;
        }

        public Builder telephone(String telephone){
            this.telephone = telephone;
            return this;
        }

        public Builder latLng(LatLng latLng){
            this.latLng = latLng;
            return this;
        }

        public PlaceViewModel build(){
            return new PlaceViewModel(this);
        }
    }
}
