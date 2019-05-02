package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class DeviceViewModel implements Parcelable {

    //region Private Properties

    private int category;

    private int modeState;

    //endregion

    //region Constructor

    @Inject
    public DeviceViewModel() {}

    //endregion

    //region Setters

    public void setCategory(int category) {
        this.category = category;
    }

    public void setModeState(int modeState) {
        this.modeState = modeState;
    }

    //endregion

    //region Getters

    public int getCategory() {
        return category;
    }

    public int getModeState() {
        return modeState;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public DeviceViewModel(Parcel in){
        this.category = in.readInt();
        this.modeState = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.category);
        out.writeInt(this.modeState);
    }

    public static final Parcelable.Creator<DeviceViewModel> CREATOR  = new Parcelable.Creator<DeviceViewModel>() {
        public DeviceViewModel createFromParcel(Parcel in) {
            return new DeviceViewModel(in);
        }

        public DeviceViewModel[] newArray(int size) {
            return new DeviceViewModel[size];
        }
    };

    //endregion

}
