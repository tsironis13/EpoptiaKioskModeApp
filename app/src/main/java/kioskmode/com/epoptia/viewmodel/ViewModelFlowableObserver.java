package kioskmode.com.epoptia.viewmodel;

import domain.com.epoptia.model.domain.DomainBaseModel;
import io.reactivex.subscribers.DisposableSubscriber;

public abstract class ViewModelFlowableObserver extends DisposableSubscriber<DomainBaseModel> {
}
