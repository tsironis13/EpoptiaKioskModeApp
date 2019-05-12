package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class WorkStationViewModel implements Parcelable {

    //region Private Properties

    private int id;

    private String name;

    //endregion

    //region Constructor

    @Inject
    public WorkStationViewModel() {}

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
    public WorkStationViewModel(Parcel in){
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
