package pt.passarola.utils.rest;

import pt.passarola.model.MetaPlaces;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public interface ApiServices {

    @GET(Endpoints.places)
    void getPlaces(Callback<MetaPlaces> callback);
}
