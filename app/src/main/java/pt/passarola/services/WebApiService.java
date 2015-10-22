package pt.passarola.services;

import pt.passarola.model.MetaPlaces;
import pt.passarola.model.events.ErrorEvent;
import pt.passarola.model.events.PlacesEvent;
import pt.passarola.utils.rest.RestApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class WebApiService {

    private RestApi restApi;
    private BusProvider busProvider;

    public WebApiService(RestApi restApi, BusProvider busProvider) {
        this.restApi = restApi;
        this.busProvider = busProvider;
    }

    public void getPlaces(){
        restApi.getApiServices().getPlaces(new Callback<MetaPlaces>() {
            @Override public void success(MetaPlaces places, Response response) {
                busProvider.post(new PlacesEvent(places.getData()));
            }

            @Override public void failure(RetrofitError error) {
                busProvider.post(new ErrorEvent(error.getMessage()));
            }
        });
    }

}
