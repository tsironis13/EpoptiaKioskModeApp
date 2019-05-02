package domain.com.epoptia.device;

import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DeviceUtility {

    /**
     * Get device category
     * todo add comments
     * @return
     */
    Single<Integer> getDeviceCategory();

    /**
     * Check if my app launcher is default
     * @return todo
     */
    Single<Boolean> isMyAppLauncherDefault();

    //todo
    Completable enableComponent(Class component);

    //todo
    Completable disableComponent(Class component);

    //todo
    Completable preventStatusBarExpansion();

    //todo
    Completable removeStatusBarOverlayCustomView();

}
