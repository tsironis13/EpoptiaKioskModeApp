package kioskmode.com.epoptia.retrofit;

import kioskmode.com.epoptia.POJO.GetWorkStationsRequest;
import kioskmode.com.epoptia.POJO.GetWorkStationsResponse;
import kioskmode.com.epoptia.POJO.GetWorkersRequest;
import kioskmode.com.epoptia.POJO.GetWorkersResponse;
import kioskmode.com.epoptia.POJO.LogoutWorkerRequest;
import kioskmode.com.epoptia.POJO.UnlockDeviceRequest;
import kioskmode.com.epoptia.POJO.UnlockDeviceResponse;
import kioskmode.com.epoptia.POJO.UploadImageResponse;
import kioskmode.com.epoptia.POJO.ValidateAdminRequest;
import kioskmode.com.epoptia.POJO.ValidateAdminResponse;
import kioskmode.com.epoptia.POJO.ValidateCustomerDomainRequest;
import kioskmode.com.epoptia.POJO.ValidateCustomerDomainResponse;
import kioskmode.com.epoptia.POJO.ValidateWorkerRequest;
import kioskmode.com.epoptia.POJO.ValidateWorkerResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @POST("actions.php")
    Call<ValidateAdminResponse> logoutWorker(@Body LogoutWorkerRequest request);

    @POST("actions.php")
    Call<UnlockDeviceResponse> unlockDevice(@Body UnlockDeviceRequest request);

    @Multipart
    @POST("actions.php")
    Call<UploadImageResponse> uploadImage(@Part("action") RequestBody action, @Part("access_token") RequestBody token, @Part("order_line_track_id") RequestBody id, @Part MultipartBody.Part image);
}
