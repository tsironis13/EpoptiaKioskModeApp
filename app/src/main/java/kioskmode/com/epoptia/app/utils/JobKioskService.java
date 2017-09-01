package kioskmode.com.epoptia.app.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by giannis on 26/8/2017.
 */

public class JobKioskService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
