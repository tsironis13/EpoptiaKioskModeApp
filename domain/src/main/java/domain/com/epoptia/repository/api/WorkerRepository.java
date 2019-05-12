package domain.com.epoptia.repository.api;

import domain.com.epoptia.model.dto.post.GetStationWorkersPostDto;
import domain.com.epoptia.model.dto.result.StationWorkersDto;
import io.reactivex.Flowable;

public interface WorkerRepository {

    Flowable<StationWorkersDto> getStationWorkers(String subDomain, GetStationWorkersPostDto stationWorkersPostDto) throws Exception;

}
