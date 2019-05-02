package kioskmode.com.epoptia.viewmodel.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import javax.inject.Inject;

public class NetworkStateViewModel extends BaseObservable {

    //region Private Properties

    private String msg;

    //endregion

    //region Constructor

    @Inject
    public NetworkStateViewModel() {}

    //endregion

    //region Setters

    public void setMsg(String msg) {
        this.msg = msg;

        notifyPropertyChanged(BR.msg);
    }

    //endregion

    //region Getters

    @Bindable
    public String getMsg() {
        return msg;
    }

    //endregion

}
