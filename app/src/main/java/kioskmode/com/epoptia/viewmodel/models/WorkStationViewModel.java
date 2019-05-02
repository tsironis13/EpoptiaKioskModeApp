package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class WorkStationViewModel implements Parcelable {

    //region Private Properties

    private int workStationId;

    private String workStationName;

    //endregion

    //region Constructor

    @Inject
    public WorkStationViewModel() {}

    //endregion

    //region Setters

    public void setWorkStationId(int workStationId) {
        this.workStationId = workStationId;
    }

    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }

    //endregion

    //region Getters

    public int getWorkStationId() {
        return workStationId;
    }

    public String getWorkStationName() {
        return workStationName;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public WorkStationViewModel(Parcel in){
        this.workStationId = in.readInt();
        this.workStationName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(workStationId);
        out.writeString(workStationName);
    }

    public static final Parcelable.Creator<WorkStationViewModel> CREATOR  = new Parcelable.Creator<WorkStationViewModel>() {
        public WorkStationViewModel createFromParcel(Parcel in) {
            return new WorkStationViewModel(in);
        }

        public WorkStationViewModel[] newArray(int size) {
            return new WorkStationViewModel[size];
        }
    };

    //endregion

}
