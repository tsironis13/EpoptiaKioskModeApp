package domain.com.epoptia.model.domain;

import java.math.BigDecimal;
import javax.inject.Inject;
import fr.bmartel.speedtest.model.SpeedTestError;

public class DomainSpeedTestModel {

    //region Private Properties

    private double mTransferRateBit;

    private SpeedTestError speedTestErrorModel;

    //endregion

    //region Constructor

    @Inject
    public DomainSpeedTestModel() {}

    //endregion

    //region Setters

    public void setTransferRateBit(double mTransferRateBit) {
        this.mTransferRateBit = mTransferRateBit;
    }

    public void setSpeedTestErrorModel(SpeedTestError speedTestErrorModel) {
        this.speedTestErrorModel = speedTestErrorModel;
    }

    //endregion

    //region Getters

    public double getTransferRateBit() {
        return mTransferRateBit;
    }

    public SpeedTestError getSpeedTestErrorModel() {
        return speedTestErrorModel;
    }

    //endregion

}
