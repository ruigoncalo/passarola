package pt.passarola.model.viewmodel;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import pt.passarola.model.ClosestPlace;
import pt.passarola.model.Place;

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
    private final int distance;
    private final String facebook;
    private final String zomato;
    private final String tripadvisor;

    private PlaceViewModel(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.fullAddress = builder.fullAddress;
        this.council = builder.council;
        this.country = builder.country;
        this.telephone = builder.telephone;
        this.latLng = builder.latLng;
        this.distance = builder.distance;
        this.facebook = builder.facebook;
        this.zomato = builder.zomato;
        this.tripadvisor = builder.tripadvisor;
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

    public int getDistance() {
        return distance;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getZomato() {
        return zomato;
    }

    public String getTripadvisor() {
        return tripadvisor;
    }

    public static class Builder {
        private String id;
        private String name;
        private String fullAddress;
        private String council;
        private String country;
        private String telephone;
        private LatLng latLng;
        private int distance;
        private String facebook;
        private String zomato;
        private String tripadvisor;

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

        public Builder distance(int distance){
            this.distance = distance;
            return this;
        }

        public Builder facebook(String facebook){
            this.facebook = facebook;
            return this;
        }

        public Builder zomato(String zomato){
            this.zomato = zomato;
            return this;
        }

        public Builder tripadvisor(String tripadvisor){
            this.tripadvisor = tripadvisor;
            return this;
        }

        public PlaceViewModel build(){
            return new PlaceViewModel(this);
        }
    }

    public static List<PlaceViewModel> createViewModelList(List<Place> places){
        List<PlaceViewModel> result = new ArrayList<>();
        for(Place place : places){
            PlaceViewModel placeViewModel = createPlaceViewModel(place);
            if(placeViewModel != null){
                result.add(placeViewModel);
            }
        }

        return result;
    }

    @Nullable
    public static PlaceViewModel createPlaceViewModel(Place place){
        PlaceViewModel result = null;
        if(place.isValid()){
            result = new Builder()
                    .id(place.getId())
                    .name(place.getName())
                    .fullAddress(place.getFullAddress())
                    .council(place.getCouncil())
                    .country(place.getCountry())
                    .telephone(place.getTelephone())
                    .latLng(place.getLatLng())
                    .facebook(place.getFacebook())
                    .zomato(place.getZomato())
                    .tripadvisor(place.getTripadvisor())
                    .build();

        }

        return result;
    }

    @Nullable
    public static PlaceViewModel createPlaceViewModel(ClosestPlace closestPlace){
        PlaceViewModel result = null;
        if(closestPlace.getPlace().isValid()){
            result = new Builder()
                    .id(closestPlace.getPlace().getId())
                    .name(closestPlace.getPlace().getName())
                    .fullAddress(closestPlace.getPlace().getFullAddress())
                    .council(closestPlace.getPlace().getCouncil())
                    .country(closestPlace.getPlace().getCountry())
                    .telephone(closestPlace.getPlace().getTelephone())
                    .latLng(closestPlace.getPlace().getLatLng())
                    .distance(closestPlace.getDistance())
                    .facebook(closestPlace.getPlace().getFacebook())
                    .zomato(closestPlace.getPlace().getZomato())
                    .tripadvisor(closestPlace.getPlace().getTripadvisor())
                    .build();

        }

        return result;
    }
}
