package domain.com.epoptia.device;

import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Single;

public interface Device {

    /**
     * Get device category
     *
     * @return
     */
    Single<DomainDeviceModel> getDeviceCategory();

}
