package kioskmode.com.epoptia.splashscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.interactor.device.GetDeviceFromLocalStorageUseCase;
import domain.com.epoptia.interactor.device.SaveDeviceCategoryToLocalStorageUseCase;
import domain.com.epoptia.interactor.user.GetUserFromLocalStorageUseCase;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.mappers.DomainDeviceModelToDeviceViewModelMapper;
import kioskmode.com.epoptia.splashscreen.helper.UserDeviceModel;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;

public class SplashScreenViewModel implements SplashScreenContract.ViewModel {

    //region Injections

    @Inject
    GetUserFromLocalStorageUseCase getUserFromLocalStorageUseCase;

    @Inject
    SaveDeviceCategoryToLocalStorageUseCase saveDeviceCategoryToLocalStorageUseCase;

    @Inject
    GetDeviceFromLocalStorageUseCase getDeviceFromLocalStorageUseCase;

    @Inject
    UserDeviceModel userDeviceModel;

    @Inject
    DomainDeviceModelToDeviceViewModelMapper domainDeviceModelToDeviceViewModelMapper;

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
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        mViewCallback = (SplashScreenContract.View) viewCallback;

        saveDeviceCategory();
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
    public void checkUserAndDeviceState() {
        Single.zip(
                getDeviceFromLocalStorageUseCase.execute(),
                getUserFromLocalStorageUseCase.execute(),
                (domainDeviceModel, domainUserModel) -> {
                    userDeviceModel.setDomainDeviceModel(domainDeviceModel);
                    userDeviceModel.setDomainUserModel(domainUserModel);

                    return userDeviceModel;
                })
                .flatMap(userDeviceModel -> domainDeviceModelToDeviceViewModelMapper.map(userDeviceModel.getDomainDeviceModel()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceViewModel -> {

                    if (userDeviceModel.getDomainUserModel().getAccessToken() == null) {
                        mViewCallback.navigateUserToLoginScreen(deviceViewModel);
                    } else {
                        if (userDeviceModel.getDomainDeviceModel().getModeState() == Constants.DEFAULT_MODE_STATE) {
                            mViewCallback.navigateUserToWorkStationsScreen(deviceViewModel);
                        } else {
                            mViewCallback.navigateUserToKioskModeScreen(deviceViewModel);
                        }
                    }
                }, (error) -> mViewCallback.onError());
    }

    //endregion

}
