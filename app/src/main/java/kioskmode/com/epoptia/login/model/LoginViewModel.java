package kioskmode.com.epoptia.login.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;

import javax.inject.Inject;

public class LoginViewModel extends BaseObservable implements Parcelable {

    //region Private Properties

    private String subDomain;

    private String username;

    private String password;

    private boolean subDomainIsValid;

    private String accessToken;

    private boolean isProcessing;

    //endregion

    //region Constructor

    @Inject
    LoginViewModel() {}

    //endregion

    //region Setters

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;

        notifyPropertyChanged(BR.subDomain);
    }

    public void setUsername(String username) {
        this.username = username;

        notifyPropertyChanged(BR.username);
    }

    public void setPassword(String password) {
        this.password = password;

        notifyPropertyChanged(BR.password);
    }

    public void setSubDomainIsValid(boolean subDomainIsValid) {
        this.subDomainIsValid = subDomainIsValid;

        notifyPropertyChanged(BR.subDomainIsValid);
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;

        notifyPropertyChanged(BR.processing);
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    //endregion

    //region Getters

    @Bindable
    public String getSubDomain() {
        return subDomain;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    @Bindable
    public boolean isSubDomainIsValid() {
        return subDomainIsValid;
    }

    @Bindable
    public boolean isProcessing() {
        return isProcessing;
    }

    public String getAccessToken() {
        return accessToken;
    }

    //endregion

    //region Parcelable Implementation

    // Parcelling part
    public LoginViewModel(Parcel in){
        this.subDomain = in.readString();
        this.subDomainIsValid = in.readByte() != 0;
        this.username = in.readString();
        this.password = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.subDomain);
        out.writeByte((byte) (this.subDomainIsValid ? 1 : 0));
        out.writeString(this.username);
        out.writeString(this.password);
    }

    public static final Parcelable.Creator<LoginViewModel> CREATOR  = new Parcelable.Creator<LoginViewModel>() {
        public LoginViewModel createFromParcel(Parcel in) {
            return new LoginViewModel(in);
        }

        public LoginViewModel[] newArray(int size) {
            return new LoginViewModel[size];
        }
    };

    //endregion

}
