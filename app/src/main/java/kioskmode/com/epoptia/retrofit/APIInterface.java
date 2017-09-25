package kioskmode.com.epoptia.retrofit;

import kioskmode.com.epoptia.POJO.GetWorkStationsRequest;
import kioskmode.com.epoptia.POJO.GetWorkStationsResponse;
import kioskmode.com.epoptia.POJO.GetWorkersRequest;
import kioskmode.com.epoptia.POJO.GetWorkersResponse;
import kioskmode.com.epoptia.POJO.ValidateAdminRequest;
import kioskmode.com.epoptia.POJO.ValidateAdminResponse;
import kioskmode.com.epoptia.POJO.ValidateCustomerDomainRequest;
import kioskmode.com.epoptia.POJO.ValidateCustomerDomainResponse;
import kioskmode.com.epoptia.POJO.ValidateWorkerRequest;
import kioskmode.com.epoptia.POJO.ValidateWorkerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by giannis on 18/9/2017.
 */

public interface APIInterface {

    @POST("actions.php")
    Call<ValidateCustomerDomainResponse> validateCstmrDomain(@Body ValidateCustomerDomainRequest request);

    @POST("actions.php")
    Call<ValidateAdminResponse> validateAdmin(@Body ValidateAdminRequest request);

    @POST("actions.php")
    Call<GetWorkStationsResponse> getWorkStations(@Body GetWorkStationsRequest request);

    @POST("actions.php")
    Call<GetWorkersResponse> getWorkers(@Body GetWorkersRequest request);

    @POST("actions.php")
    Call<ValidateWorkerResponse> validateWorker(@Body ValidateWorkerRequest request);
}
