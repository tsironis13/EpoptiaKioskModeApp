package kioskmode.com.epoptia.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.client.ValidateClientSubDomainUseCase;
import domain.com.epoptia.interactor.user.LoginAdminUseCase;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.ObserverContextStrategy;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.observercreators.LoginAdminViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.observercreators.ValidateClientSubDomainViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.observerstrategy.CompletableObserverStrategy;

public class LoginViewModel implements LoginContract.ViewModel {

    //region Injections

    @Inject
    ValidateClientSubDomainUseCase validateClientSubDomainUseCase;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    LoginAdminUseCase loginAdminUseCase;

    @Inject
    ValidateClientSubDomainPostDto validateClientSubDomainDto;

    @Inject
    LoginAdminPostDto loginAdminPostDto;

    @Inject
    kioskmode.com.epoptia.viewmodel.models.LoginViewModel loginViewModel;

    @Inject
    ObserverContextStrategy observerContextStrategy;

    //endregion

    //region Private Properties

    private static final String debugTag = LoginViewModel.class.getSimpleName();

    private Disposable mObserverDisposable;

    private CompletableSubject mCompletableProcessor;

    private LoginContract.View mViewCallback;

    private ViewModelObserverCreator mViewModelObserverCreator;

    //10 -> running
    //0 -> none
    private int requestState;

    private int subjectType;

    //endregion

    //region Constructor

    @Inject
    public LoginViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        mViewCallback = (LoginContract.View) viewCallback;

        if (savedInstanceState == null) {
            initializeFormData();
        }
    }

    @Override
    public void onViewResumed() {
        //Log.e(debugTag, "VIEW RESULMED => " + requestState);
        if (requestState == Constants.REQUEST_RUNNING) {
            Log.e(debugTag, "onViewResumed => request pending");

            Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON VIEW RESUMED OBSERVER");

            mViewCallback.setProcessing(true);
            //todo complete for all subject types
            if (subjectType == Constants.COMPLETABLE_SUBJECT) {
                observerContextStrategy.setStrategy(new CompletableObserverStrategy(mCompletableProcessor, mViewModelObserverCreator));
            }

            mObserverDisposable = observerContextStrategy.executeStrategy();
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

                mViewCallback.bindLoginViewModelToViewModel(loginViewModel);
            }
        }, (error) -> {
            //todo add error handling
        });
    }

    @Override
    public void submitForm(kioskmode.com.epoptia.viewmodel.models.LoginViewModel loginViewModel) {
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
    public void loginAdmin(kioskmode.com.epoptia.viewmodel.models.LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        subjectType = Constants.COMPLETABLE_SUBJECT;

        requestState = Constants.REQUEST_RUNNING;

        loginAdminPostDto.setClientSubDomain(loginViewModel.getSubDomain());
        loginAdminPostDto.setUsername(loginViewModel.getUsername());
        loginAdminPostDto.setPassword(loginViewModel.getPassword());
        loginAdminPostDto.setAction(Constants.ACTION_VALIDATE_ADMIN);

        mCompletableProcessor = CompletableSubject.create();

        mViewModelObserverCreator = new LoginAdminViewModelObserverCreator(this);

        Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON SUBMIT");

        mObserverDisposable = mCompletableProcessor
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());

        loginAdminUseCase
                    .execute(loginAdminPostDto)
                    .doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(mCompletableProcessor);
    }

    @SuppressLint("CheckResult")
    @Override
    public void validateClientSubDomain(kioskmode.com.epoptia.viewmodel.models.LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        subjectType = Constants.COMPLETABLE_SUBJECT;

        requestState = Constants.REQUEST_RUNNING;

        validateClientSubDomainDto.setClientSubDomain(loginViewModel.getSubDomain());
        validateClientSubDomainDto.setAction(Constants.ACTION_VALIDATE_CUSTOMER_DOMAIN);

        mCompletableProcessor = CompletableSubject.create();

        mViewModelObserverCreator = new ValidateClientSubDomainViewModelObserverCreator(this);

        Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON SUBMIT");

        mObserverDisposable = mCompletableProcessor
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());

        validateClientSubDomainUseCase
                                .execute(validateClientSubDomainDto)
                                .doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(mCompletableProcessor);
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

        requestState = Constants.REQUEST_NONE;
    }

    //endregion

}
