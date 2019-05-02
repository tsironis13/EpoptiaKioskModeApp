package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class WorkerViewModel implements Parcelable {

    //region Private Properties

    private int workerId;

    private String workerName;

    //endregion

    //region Constructor

    @Inject
    public WorkerViewModel() {}

    //endregion

    //region Setters

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    //endregion

    //region Getters

    public int getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public WorkerViewModel(Parcel in){
        this.workerId = in.readInt();
        this.workerName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(workerId);
        out.writeString(workerName);
    }

    public static final Parcelable.Creator<WorkerViewModel> CREATOR  = new Parcelable.Creator<WorkerViewModel>() {
        public WorkerViewModel createFromParcel(Parcel in) {
            return new WorkerViewModel(in);
        }

        public WorkerViewModel[] newArray(int size) {
            return new WorkerViewModel[size];
        }
    };

    //endregion

}
