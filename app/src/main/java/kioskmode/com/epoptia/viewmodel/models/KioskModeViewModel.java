package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class KioskModeViewModel implements Parcelable {

    //region Private Properties

    private WorkStationViewModel workStationViewModel;

    private DeviceViewModel deviceViewModel;

    private WorkerPanelViewModel workerPanelViewModel;

    private WorkerViewModel workerViewModel;

    //endregion

    //region Constructor

    @Inject
    public KioskModeViewModel() {}

    //endregion

    //region Setters

    public void setDeviceViewModel(DeviceViewModel deviceViewModel) {
        this.deviceViewModel = deviceViewModel;
    }

    public void setWorkStationViewModel(WorkStationViewModel workStationViewModel) {
        this.workStationViewModel = workStationViewModel;
    }

    public void setWorkerPanelViewModel(WorkerPanelViewModel workerPanelViewModel) {
        this.workerPanelViewModel = workerPanelViewModel;
    }

    public void setWorkerViewModel(WorkerViewModel workerViewModel) {
        this.workerViewModel = workerViewModel;
    }

    //endregion

    //region Getters

    public WorkStationViewModel getWorkStationViewModel() {
        return workStationViewModel;
    }

    public DeviceViewModel getDeviceViewModel() {
        return deviceViewModel;
    }

    public WorkerPanelViewModel getWorkerPanelViewModel() {
        return workerPanelViewModel;
    }

    public WorkerViewModel getWorkerViewModel() {
        return workerViewModel;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public KioskModeViewModel(Parcel in){
        this.workStationViewModel = in.readParcelable(WorkStationViewModel.class.getClassLoader());
        this.deviceViewModel = in.readParcelable(DeviceViewModel.class.getClassLoader());
        this.workerPanelViewModel = in.readParcelable(WorkerPanelViewModel.class.getClassLoader());
        this.workerViewModel = in.readParcelable(WorkerViewModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.workStationViewModel, flags);
        out.writeParcelable(this.deviceViewModel, flags);
        out.writeParcelable(this.workerPanelViewModel, flags);
        out.writeParcelable(this.workerViewModel, flags);
    }

    public static final Parcelable.Creator<KioskModeViewModel> CREATOR  = new Parcelable.Creator<KioskModeViewModel>() {
        public KioskModeViewModel createFromParcel(Parcel in) {
            return new KioskModeViewModel(in);
        }

        public KioskModeViewModel[] newArray(int size) {
            return new KioskModeViewModel[size];
        }
    };

    //endregion

}
