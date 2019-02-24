package data.com.epoptia.network.service;

import android.content.Context;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.ClientService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class BaseServiceWrapper {

    //region Injections

    @Inject
    BaseService baseService;

    @Inject
    ClientService clientService;

    //endregion

    //region Constructor

    @Inject
    public BaseServiceWrapper() {}

    //endregion

    //region Public Methods

    public ServiceAPI getServiceAPI(String subDomain) throws Exception {
        if (subDomain == null) {
            throw new Exception("Please provide a sub domain.", new NullPointerException());
        }

        HttpLoggingInterceptor httpLoggingInterceptor = baseService.provideHttpLoggingInterceptor();

        OkHttpClient okHttpClient = baseService.provideOkHttpClient(httpLoggingInterceptor);

        String baseUrl = "http://" + subDomain + ".epoptia.com/mobile/";

        Retrofit retrofit = baseService.provideRetrofit(okHttpClient, baseUrl);

        return baseService.provideServiceAPI(retrofit);
    }

    //endregion

}
