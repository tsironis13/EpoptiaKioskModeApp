package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainNetworkStateModel;
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

    public NetworkStateViewModel map(DomainNetworkStateModel domainNetworkStateModel) {
        networkStateViewModel.setMsg(domainNetworkStateModel.getMsg());

        return networkStateViewModel;
    }

    //endregion

}
