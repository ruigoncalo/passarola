package pt.passarola.services.rest;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RestAdapter;

/**
 * Created by ruigoncalo on 22/10/15.
 */
@Singleton
public class RestApi {

    public static boolean DEBUG = true;
    private ApiServices apiServices;

    @Inject
    public RestApi() {
        RestAdapter restAdapter = build(Endpoints.baseUrl);
        apiServices = restAdapter.create(ApiServices.class);
    }

    public RestAdapter build(String baseUrl) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .build();

        if (DEBUG) {
            adapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        return adapter;
    }

    public ApiServices getApiServices() {
        return apiServices;
    }
}
