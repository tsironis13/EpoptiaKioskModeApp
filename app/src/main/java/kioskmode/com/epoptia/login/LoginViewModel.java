package kioskmode.com.epoptia.login;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;

import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.client.ValidateClientSubDomainUseCase;
import domain.com.epoptia.interactor.device.CheckDeviceCategoryUseCase;
import domain.com.epoptia.interactor.user.LoginAdminUseCase;
import domain.com.epoptia.interactor.user.SaveAccessTokenToLocalStorageUseCase;
import domain.com.epoptia.interfaces.DeviceCategory;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;
import io.reactivex.subjects.Subject;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.mappers.LoginViewModelToUserDomainModelMapper;
import kioskmode.com.epoptia.viewmodel.ContextStr;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.creators.LoginAdminViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.creators.ValidateClientSubDomainViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.strategy.CompletableStrategy;

public class LoginViewModel implements LoginContract.ViewModel, DeviceCategory {

    //region Injections

    @Inject
    ValidateClientSubDomainUseCase validateClientSubDomainUseCase;

    @Inject
    SaveAccessTokenToLocalStorageUseCase saveAccessTokenToLocalStorageUseCase;

    @Inject
    CheckDeviceCategoryUseCase checkDeviceCategoryUseCase;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    LoginAdminUseCase loginAdminUseCase;

    @Inject
    ValidateClientSubDomainPostDto validateClientSubDomainDto;

    @Inject
    LoginAdminPostDto loginAdminPostDto;

    @Inject
    LoginViewModelToUserDomainModelMapper loginViewModelToUserDomainModelMapper;

    @Inject
    kioskmode.com.epoptia.login.model.LoginViewModel loginViewModel;

    @Inject
    ContextStr contextStr;

    //endregion

    //region Private Properties

    private static final String debugTag = LoginViewModel.class.getSimpleName();

    private Disposable mObserverDisposable;

    private CompletableSubject mProcessor;

    private LoginContract.View mViewCallback;

    private ViewModelObserverCreator mViewModelObserverCreator;

    //todo add constants
    //10 -> running
    //0 -> none
    private int requestState;

    //todo add constants
    private int subjectType;

    //endregion

    //region Constructor

    @Inject
    public LoginViewModel() { }

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        mViewCallback = (LoginContract.View) viewCallback;

        checkDeviceCategory();

        initializeFormData();
    }

    @Override
    public void onViewResumed() {
        //Log.e(debugTag, "VIEW RESULMED => " + requestState);
        if (requestState == 10) {
            Log.e(debugTag, "onViewResumed => request pending");

            Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON VIEW RESUMED OBSERVER");

            mViewCallback.setProcessing(true);
            //todo complete for all subject types
            if (subjectType == 1) {
                contextStr.setStrategy(new CompletableStrategy(mProcessor, mViewModelObserverCreator));
            }

            mObserverDisposable = contextStr.executeStrategy();
        }
    }

    @Override
    public void onViewDetached() {
        mViewCallback = null;

        if (mObserverDisposable != null) {
            Log.e(debugTag, "onViewDetached");

            mObserverDisposable.dispose();

            mObserverDisposable = null;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void initializeFormData() {
        getClientFromLocalStorageUseCase.execute().subscribe((client) -> {
            if (client.getSubDomain() != null) {
                loginViewModel.setSubDomainIsValid(true);
                loginViewModel.setSubDomain(client.getSubDomain());

                if (mViewCallback == null) {
                    return;
                }

                mViewCallback.initializeSubDomain(loginViewModel);
            }
        }, (error) -> {
            //todo add error handling
            int x = 10;
        });
    }

    @Override
    public void checkDeviceCategory() {
        checkDeviceCategoryUseCase.execute(this);
    }

    @Override
    public void submitForm(kioskmode.com.epoptia.login.model.LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        if (!loginViewModel.isSubDomainIsValid()) {
            validateClientSubDomain(loginViewModel);
        } else {
            loginAdmin(loginViewModel);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void loginAdmin(kioskmode.com.epoptia.login.model.LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        //todo add constant
        //single
        subjectType = 1;

        requestState = 10;

        loginAdminPostDto.setClientSubDomain(loginViewModel.getSubDomain());
        loginAdminPostDto.setUsername(loginViewModel.getUsername());
        loginAdminPostDto.setPassword(loginViewModel.getPassword());
        loginAdminPostDto.setAction("validate_admin");

        mProcessor = CompletableSubject.create();

        mViewModelObserverCreator = new LoginAdminViewModelObserverCreator(this);

        Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON SUBMIT");

        mObserverDisposable = mProcessor
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());

        loginAdminUseCase
                    .execute(loginAdminPostDto)
                    .doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(mProcessor);
    }

    @Override
    public void validateClientSubDomain(kioskmode.com.epoptia.login.model.LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        //todo add constant
        //completable
        subjectType = 1;

        //todo add constant
        requestState = 10;

        validateClientSubDomainDto.setClientSubDomain(loginViewModel.getSubDomain());
        validateClientSubDomainDto.setAction("validate_customer_domain");

        mProcessor = CompletableSubject.create();

        mViewModelObserverCreator = new ValidateClientSubDomainViewModelObserverCreator(this);

        Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON SUBMIT");

        mObserverDisposable = mProcessor
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());

        validateClientSubDomainUseCase
                                .execute(validateClientSubDomainDto)
                                .doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(mProcessor);
    }

//    @Override
//    public void saveAccessToken(kioskmode.com.epoptia.login.viewmodel.LoginViewModel loginViewModel) {
//        if (loginViewModel == null) {
//            return;
//        }
//
//        saveAccessTokenToLocalStorageUseCase
//                                        .execute(loginViewModelToUserDomainModelMapper.map(loginViewModel))
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe();
//    }

    @Override
    public void deviceIsTablet() {
        if (mViewCallback == null) {
            return;
        }

        mViewCallback.deviceIsTablet();
    }

    @Override
    public void deviceIsPhone() {
        if (mViewCallback == null) {
            return;
        }

        mViewCallback.deviceIsPhone();
    }

    @Override
    public void validateClientSubDomainOnSuccess() {
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.validateClientSubDomainOnSuccess();
    }

    @Override
    public void validateClientSubDomainOnError(Throwable throwable) {
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.validateClientSubDomainOnError(throwable);
    }

    @Override
    public void loginAdminOnSuccess() {
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.loginAdminOnSuccess();
    }

    @Override
    public void loginAdminOnError(Throwable throwable) {
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.loginAdminOnError(throwable);
    }

    //endregion

    //region Private Methods

    private void clearUnnecessaryReferencesAndUnsetRequestState() {
        mObserverDisposable = null;

        mViewModelObserverCreator = null;

        mViewCallback.setProcessing(false);

        requestState = 0;
    }

    //endregion

}
