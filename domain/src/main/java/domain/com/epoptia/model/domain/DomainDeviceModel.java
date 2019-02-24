package domain.com.epoptia.model.domain;

import javax.inject.Inject;

public class DomainDeviceModel {

    //region Private Properties

    private int category;

    private int modeState;

    //endregion

    //region Constructor

    @Inject
    public DomainDeviceModel() {}

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

}
