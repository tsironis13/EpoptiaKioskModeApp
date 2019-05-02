package data.com.epoptia.api.repository;

import javax.inject.Inject;

import data.com.epoptia.api.service.BaseNetworkServiceWrapper;
import domain.com.epoptia.model.dto.post.UnlockDevicePostDto;
import domain.com.epoptia.model.dto.result.BaseResponseDto;
import domain.com.epoptia.repository.api.DeviceRepository;
import io.reactivex.Single;

public class DeviceRepositoryImpl implements DeviceRepository {

    //region Injections

    @Inject
    BaseNetworkServiceWrapper baseNetworkServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public DeviceRepositoryImpl() {}

    //endregion

    //region Public Methods


    @Override
    public Single<BaseResponseDto> unlockDevice(String subDomain, UnlockDevicePostDto unlockDevicePostDto) throws Exception {
        return baseNetworkServiceWrapper.getServiceAPI(subDomain).unlockDevice(unlockDevicePostDto);
    }

    //endregion

}
