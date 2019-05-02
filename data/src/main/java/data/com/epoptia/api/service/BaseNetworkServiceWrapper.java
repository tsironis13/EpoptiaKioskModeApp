package data.com.epoptia.api.service;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.ClientService;
import domain.com.epoptia.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class BaseNetworkServiceWrapper {

    //region Injections

    @Inject
    BaseNetworkProviderService baseNetworkProviderService;

    @Inject
    ClientService clientService;

    //endregion

    //region Constructor

    @Inject
    public BaseNetworkServiceWrapper() {}

    //endregion

    //region Public Methods

    public ServiceAPI getServiceAPI(String subDomain) throws Exception {
        if (subDomain == null) {
            throw new Exception(Constants.INVALID_SUBDOMAIN, new NullPointerException());
        }

        HttpLoggingInterceptor httpLoggingInterceptor = baseNetworkProviderService.provideHttpLoggingInterceptor();

        OkHttpClient okHttpClient = baseNetworkProviderService.provideOkHttpClient(httpLoggingInterceptor);

        //todo change formatting ??
        String baseUrl = "http://" + subDomain + ".epoptia.com/mobile/";

        Retrofit retrofit = baseNetworkProviderService.provideRetrofit(okHttpClient, baseUrl);

        return baseNetworkProviderService.provideServiceAPI(retrofit);
    }

    //endregion

}
