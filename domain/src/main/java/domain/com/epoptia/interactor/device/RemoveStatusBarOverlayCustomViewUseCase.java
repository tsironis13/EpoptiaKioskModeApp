package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.device.DeviceUtility;
import domain.com.epoptia.interactor.type.CompletableUseCase;
import io.reactivex.Completable;

public class RemoveStatusBarOverlayCustomViewUseCase implements CompletableUseCase {

    //region Injections

    @Inject
    DeviceUtility deviceUtility;

    //endregion

    //region Constructor

    @Inject
    public RemoveStatusBarOverlayCustomViewUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute() {
        return deviceUtility.removeStatusBarOverlayCustomView();
    }

    //endregion

}
