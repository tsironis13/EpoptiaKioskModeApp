package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class WorkerViewModel implements Parcelable {

    //region Private Properties

    private int id;

    private String name;

    //endregion

    //region Constructor

    @Inject
    public WorkerViewModel() {}

    //endregion

    //region Setters

    public void setId(int workerId) {
        this.id = workerId;
    }

    public void setName(String workerName) {
        this.name = workerName;
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public WorkerViewModel(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
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
