package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainNetworkStateModel;
import io.reactivex.Single;
import kioskmode.com.epoptia.viewmodel.models.NetworkStateViewModel;

public class DomainNetworkStateModelToNetworkStateViewModelMapper {

    //region Injections

    @Inject
    NetworkStateViewModel networkStateViewModel;

    //endregion

    //region Constructor

    @Inject
    public DomainNetworkStateModelToNetworkStateViewModelMapper() {}

    //endregion

    //region Public Methods

    public Single<NetworkStateViewModel> map(DomainNetworkStateModel domainNetworkStateModel) {
        networkStateViewModel.setMsg(domainNetworkStateModel.getMsg());

        return Single.just(networkStateViewModel);
    }

    //endregion

}
