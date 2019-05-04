package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Single;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;

public class DomainDeviceModelToDeviceViewModelMapper {

    //region Injections

    @Inject
    DeviceViewModel deviceViewModel;

    //endregion

    //region Constructor

    @Inject
    public DomainDeviceModelToDeviceViewModelMapper() {}

    //endregion

    //region Public Methods

    public Single<DeviceViewModel> map(DomainDeviceModel domainDeviceModel) {
        deviceViewModel.setCategory(domainDeviceModel.getCategory());
        deviceViewModel.setModeState(domainDeviceModel.getModeState());

        return Single.just(deviceViewModel);
    }

    //endregion

}
