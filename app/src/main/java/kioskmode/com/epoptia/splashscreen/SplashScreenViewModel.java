package kioskmode.com.epoptia.splashscreen;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;

import domain.com.epoptia.interactor.device.GetDeviceFromLocalStorageUseCase;
import domain.com.epoptia.interactor.device.SaveDeviceCategoryToLocalStorageUseCase;
import domain.com.epoptia.interactor.device.SaveDeviceModeStateToLocalStorageUseCase;
import domain.com.epoptia.interactor.user.GetUserFromLocalStorageUseCase;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.splashscreen.helper.UserDeviceModel;

public class SplashScreenViewModel implements SplashScreenContract.ViewModel {

    //region Injections

    @Inject
    GetUserFromLocalStorageUseCase getUserFromLocalStorageUseCase;

    @Inject
    SaveDeviceCategoryToLocalStorageUseCase saveDeviceCategoryToLocalStorageUseCase;

    @Inject
    SaveDeviceModeStateToLocalStorageUseCase saveDeviceModeStateToLocalStorageUseCase;

    @Inject
    GetDeviceFromLocalStorageUseCase getDeviceFromLocalStorageUseCase;

    @Inject
    UserDeviceModel userDeviceModel;

    //endregion

    //region Private Properties

    private SplashScreenContract.View mViewCallback;

    //endregion

    //region Constructor

    @Inject
    public SplashScreenViewModel() { }

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        mViewCallback = (SplashScreenContract.View) viewCallback;
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewDetached() {
        mViewCallback = null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void saveDeviceCategory() {
        saveDeviceCategoryToLocalStorageUseCase
                                        .execute()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            //app is on background
                                            if (mViewCallback == null) {
                                                return;
                                            }

                                            mViewCallback.onSaveDeviceCategorySuccess();
                                        }, (error) -> {
                                            //app is on background
                                            if (mViewCallback == null) {
                                                return;
                                            }

                                            mViewCallback.onError();
                                        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void checkUserIsAuthenticated() {
        Single.zip(
                getDeviceFromLocalStorageUseCase.execute(),
                getUserFromLocalStorageUseCase.execute(),
                (domainDeviceModel, domainUserModel) -> {
                    userDeviceModel.setDeviceModeState(domainDeviceModel.getModeState());
                    userDeviceModel.setUserAccessToken(domainUserModel.getAccessToken());

                    return userDeviceModel;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userDeviceModel -> {
                    Log.e("sds", "sdsdsds2332" + userDeviceModel.getUserAccessToken());

                    if (userDeviceModel.getUserAccessToken() == null) {
                        mViewCallback.navigateUserToLoginScreen();
                    } else {
                        if (userDeviceModel.getDeviceModeState() == 15) {
                            mViewCallback.navigateUserToWorkStationsScreen();
                        } else {
                            mViewCallback.navigateUserToKioskModeScreen();
                        }
                    }
                }, (error) -> {
                    Log.e("sds", "sdsdsds1");
                    mViewCallback.onError();
                });
    }

    //endregion

}
