package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import javax.inject.Inject;

public class StationWorkersViewModel extends BaseObservable implements Parcelable {

    //region Private Properties

    private WorkStationViewModel workStationViewModel;

    //endregion

    //region Constructor

    @Inject
    public StationWorkersViewModel() {}

    //endregion

    //region Setters

    public void setWorkStationViewModel(WorkStationViewModel workStationViewModel) {
        this.workStationViewModel = workStationViewModel;
    }

    //endregion

    //region Getters

    public WorkStationViewModel getWorkStationViewModel() {
        return workStationViewModel;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public StationWorkersViewModel(Parcel in){
        this.workStationViewModel = in.readParcelable(WorkStationViewModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.workStationViewModel, flags);
    }

    public static final Parcelable.Creator<StationWorkersViewModel> CREATOR  = new Parcelable.Creator<StationWorkersViewModel>() {
        public StationWorkersViewModel createFromParcel(Parcel in) {
            return new StationWorkersViewModel(in);
        }

        public StationWorkersViewModel[] newArray(int size) {
            return new StationWorkersViewModel[size];
        }
    };

    //endregion

}
