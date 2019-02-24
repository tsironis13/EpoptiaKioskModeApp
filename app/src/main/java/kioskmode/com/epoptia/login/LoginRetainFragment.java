package kioskmode.com.epoptia.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

public class LoginRetainFragment extends Fragment {

    //region Private Properties

    private LoginContract.ViewModel loginViewModel;

    //endregion

    //region Constructor

    @Inject
    public LoginRetainFragment() {}

    //endregion

    //region Lifecycle Methods

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    //endregion

    //region Public Methods

    public void retainViewModel(LoginContract.ViewModel viewModel) {
        this.loginViewModel = viewModel;
    }

    public LoginContract.ViewModel getViewModel() {
        return loginViewModel;
    }

    //endregion

}
