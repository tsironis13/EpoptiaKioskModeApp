package data.com.epoptia.api.service;

import domain.com.epoptia.model.dto.post.GetStationWorkersPostDto;
import domain.com.epoptia.model.dto.post.GetWorkStationsPostDto;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.post.UnlockDevicePostDto;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import domain.com.epoptia.model.dto.result.BaseResponseDto;
import domain.com.epoptia.model.dto.result.StationWorkersDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.model.dto.result.WorkStationsDto;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceAPI {

    @POST("actions.php")
    Single<UserDto> validateClientSubDomain(@Body ValidateClientSubDomainPostDto validateClientSubDomainPostDto);

    @POST("actions.php")
    Single<UserDto> loginAdmin(@Body LoginAdminPostDto loginAdminPostDto);

    @POST("actions.php")
    Flowable<WorkStationsDto> getWorkStations(@Body GetWorkStationsPostDto workStationsPostDto);

    @POST("actions.php")
    Single<BaseResponseDto> unlockDevice(@Body UnlockDevicePostDto unlockDevicePostDto);

    @POST("actions.php")
    Flowable<StationWorkersDto> getStationWorkers(@Body GetStationWorkersPostDto stationWorkersPostDto);

//    @POST("actions.php")
//    Call<ValidateCustomerDomainResponse> validateCstmrDomain(@Body ValidateCustomerDomainRequest request);
//
//    @POST("actions.php")
//    Call<ValidateAdminResponse> validateAdmin(@Body ValidateAdminRequest request);
//
//    @POST("actions.php")
//    Call<GetWorkStationsResponse> getWorkStations(@Body GetWorkStationsRequest request);
//
//    @POST("actions.php")
//    Call<GetWorkersResponse> getWorkers(@Body GetWorkersRequest request);
//
//    @POST("actions.php")
//    Call<ValidateWorkerResponse> validateWorker(@Body ValidateWorkerRequest request);
//
//    @POST("actions.php")
//    Call<ValidateAdminResponse> logoutWorker(@Body LogoutWorkerRequest request);
//
//    @POST("actions.php")
//    Call<UnlockDeviceResponse> unlockDevice(@Body UnlockDeviceRequest request);
//
//    @Multipart
//    @POST("actions.php")
//    Call<UploadImageResponse> uploadImage(@Part("action") RequestBody action, @Part("access_token") RequestBody token, @Part("order_line_track_id") RequestBody id, @Part MultipartBody.Part image);

}
