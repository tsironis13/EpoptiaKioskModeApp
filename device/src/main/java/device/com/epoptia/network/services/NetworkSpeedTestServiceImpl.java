package device.com.epoptia.network.services;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import domain.com.epoptia.device.network.services.NetworkSpeedTestService;
import domain.com.epoptia.model.domain.DomainSpeedTestModel;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NetworkSpeedTestServiceImpl implements NetworkSpeedTestService {

    //region Injections

    //endregion

    //region Private Properties

    private SpeedTestSocket speedTestSocket;

    private DomainSpeedTestModel domainSpeedTestModel;

    private Disposable disposable;

    private ISpeedTestListener iSpeedTestListener;

    //endregion

    //region Constructor

    public NetworkSpeedTestServiceImpl(SpeedTestSocket speedTestSocket, DomainSpeedTestModel domainSpeedTestModel) {
        this.speedTestSocket = speedTestSocket;

        this.domainSpeedTestModel = domainSpeedTestModel;
    }

    //endregion

    //region Public Methods

    @SuppressLint("CheckResult")
    @Override
    public void startService() {
        Log.e("a", "START SERVICE");
        disposable = Completable
                            .fromAction(() -> speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso"))
                            .subscribeOn(Schedulers.io())
                            .repeatWhen(objectFlowable -> objectFlowable.delay(15000, TimeUnit.MILLISECONDS)).subscribe();
    }

    @Override
    public Flowable<DomainSpeedTestModel> addSpeedTestListener() {
        return Flowable.create(emitter -> {

            iSpeedTestListener = new ISpeedTestListener() {
                @Override
                public void onCompletion(SpeedTestReport report) {
                    if (disposable.isDisposed()) {
                        //todo remove
                        Log.e("a", "COMPLETED");
                        emitter.onComplete();

                        return;
                    }

                    double transferRateBit = convertBitPerSecondToMegabitPerSecond(report.getTransferRateBit().doubleValue());

                    domainSpeedTestModel.setTransferRateBit(transferRateBit);
                    domainSpeedTestModel.setSpeedTestErrorModel(null);

                    //todo remove
                    Log.e("a", "iSpeedTestListener completion ON NEXT");
                    emitter.onNext(domainSpeedTestModel);
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    if (disposable.isDisposed()) {
                        emitter.onComplete();

                        return;
                    }

                    domainSpeedTestModel.setSpeedTestErrorModel(speedTestError);

                    emitter.onNext(domainSpeedTestModel);
                }
            };

            speedTestSocket.addSpeedTestListener(iSpeedTestListener);
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public void stopService() {
        if (disposable != null) {
            speedTestSocket.removeSpeedTestListener(iSpeedTestListener);

            Log.e("a", "DISPOSED");

            disposable.dispose();
        }
    }

    //endregion

    //region Private Methods

    private double convertBitPerSecondToMegabitPerSecond(double bitPerSecond) {
        return bitPerSecond * 0.000001;
    }

    //endregion
}
