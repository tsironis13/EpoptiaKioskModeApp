package device.com.epoptia;

import android.content.Context;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.device.Device;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Single;

public class DeviceImpl implements Device {

    //region Injections

    //todo remove
//    @Inject
//    domain.com.epoptia.model.domain.DomainDeviceModel deviceModel;

    //endregion

    //region Private Properties

    private Context mContext;

    private DomainDeviceModel device;

    //endregion

    //region Constructor

    @Inject
    public DeviceImpl(Context context, DomainDeviceModel device) {
        this.mContext = context;
        this.device = device;
    }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainDeviceModel> getDeviceCategory() {
        if (mContext.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            device.setCategory(Constants.TABLET);

            return Single.just(device);
        }

        device.setCategory(Constants.PHONE);

        return Single.just(device);
    }

    //#endregion

}
