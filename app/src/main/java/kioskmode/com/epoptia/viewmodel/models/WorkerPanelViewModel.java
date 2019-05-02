package kioskmode.com.epoptia.viewmodel.models;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

public class WorkerPanelViewModel implements Parcelable {

    //region Private Properties

    private String cookie;

    private String url;

    //endregion

    //region Constructor

    @Inject
    public WorkerPanelViewModel() {}

    //endregion

    //region Setters

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //endregion

    //region Getters

    public String getCookie() {
        return cookie;
    }

    public String getUrl() {
        return url;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public WorkerPanelViewModel(Parcel in){
        this.cookie = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(cookie);
        out.writeString(url);
    }

    public static final Parcelable.Creator<WorkerPanelViewModel> CREATOR  = new Parcelable.Creator<WorkerPanelViewModel>() {
        public WorkerPanelViewModel createFromParcel(Parcel in) {
            return new WorkerPanelViewModel(in);
        }

        public WorkerPanelViewModel[] newArray(int size) {
            return new WorkerPanelViewModel[size];
        }
    };

    //endregion

}
