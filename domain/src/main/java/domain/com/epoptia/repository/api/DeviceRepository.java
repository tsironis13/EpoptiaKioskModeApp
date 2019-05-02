package domain.com.epoptia.repository.api;

import domain.com.epoptia.model.dto.post.UnlockDevicePostDto;
import domain.com.epoptia.model.dto.result.BaseResponseDto;
import io.reactivex.Single;

public interface DeviceRepository {

    Single<BaseResponseDto> unlockDevice(String subDomain, UnlockDevicePostDto unlockDevicePostDto) throws Exception;

}
