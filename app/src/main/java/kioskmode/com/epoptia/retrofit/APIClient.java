package kioskmode.com.epoptia.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giannis on 18/9/2017.
 */

public class APIClient {

    private String subdomain;
    public String URL = "http://"+subdomain+".epoptia.com/mobile/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String subdomain) {
        String URL = "http://"+subdomain+".epoptia.com/mobile/";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }

}
