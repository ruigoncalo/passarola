package pt.passarola.services;

import pt.passarola.model.MetaPlaces;
import pt.passarola.services.rest.RestApi;
import retrofit.Callback;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class WebApiService {

    private RestApi restApi;

    public WebApiService(RestApi restApi) {
        this.restApi = restApi;
    }

    public void getPlaces(Callback<MetaPlaces> callback){
        restApi.getApiServices().getPlaces(callback);
    }

}
