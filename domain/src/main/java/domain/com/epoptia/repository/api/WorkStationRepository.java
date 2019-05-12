package domain.com.epoptia.repository.api;

import domain.com.epoptia.model.dto.post.GetWorkStationsPostDto;
import domain.com.epoptia.model.dto.result.WorkStationsDto;
import io.reactivex.Flowable;

public interface WorkStationRepository {

    Flowable<WorkStationsDto> getWorkStations(String subDomain, GetWorkStationsPostDto workStationsPostDto) throws Exception;

}
