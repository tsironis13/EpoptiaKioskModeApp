package domain.com.epoptia.mappers;

import java.util.List;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainBaseModel;
import domain.com.epoptia.model.domain.DomainWorkerModel;
import io.reactivex.Single;

public class ListDomainWorkerToDomainBaseModelMapper {

    //region Injections

    @Inject
    DomainBaseModel domainBaseModel;

    //endregion

    //region Constructor

    @Inject
    public ListDomainWorkerToDomainBaseModelMapper() {}

    //endregion

    //region Public Methods

    public Single<DomainBaseModel> map(List<DomainWorkerModel> workers) {
        domainBaseModel.setWorkerModels(workers);

        return Single.just(domainBaseModel);
    }

    //endregion

}
