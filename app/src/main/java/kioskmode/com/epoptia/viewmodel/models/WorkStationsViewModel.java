package kioskmode.com.epoptia.viewmodel.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class WorkStationsViewModel extends BaseObservable implements Parcelable {

    //region Private Properties

    private DeviceViewModel deviceViewModel;

    private boolean isProcessing;

    private String errorMsg;

    //endregion

    //region Constructor

    @Inject
    public WorkStationsViewModel() {}

    //endregion

    //region Setters

    public void setDeviceViewModel(DeviceViewModel deviceViewModel) {
        this.deviceViewModel = deviceViewModel;
    }

    public void setProcessing(boolean processing) {
        this.isProcessing = processing;

        notifyPropertyChanged(BR.processing);
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;

        notifyPropertyChanged(BR.errorMsg);
    }

    //endregion

    //region Getters

    public DeviceViewModel getDeviceViewModel() {
        return deviceViewModel;
    }

    @Bindable
    public boolean isProcessing() {
        return isProcessing;
    }

    @Bindable
    public String getErrorMsg() {
        return errorMsg;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public WorkStationsViewModel(Parcel in){
        this.deviceViewModel = in.readParcelable(DeviceViewModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.deviceViewModel, flags);
    }

    public static final Parcelable.Creator<WorkStationsViewModel> CREATOR  = new Parcelable.Creator<WorkStationsViewModel>() {
        public WorkStationsViewModel createFromParcel(Parcel in) {
            return new WorkStationsViewModel(in);
        }

        public WorkStationsViewModel[] newArray(int size) {
            return new WorkStationsViewModel[size];
        }
    };

    //endregion

}
