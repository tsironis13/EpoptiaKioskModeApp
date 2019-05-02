package kioskmode.com.epoptia.adapters;

import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import kioskmode.com.epoptia.R;

/**
 * Created by giannis on 5/9/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    //region Private Properties

    private ViewDataBinding mBinding;

    //private RecyclerCallback bindingInterface;

    //endregion

    //region Constructor

    public RecyclerViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());

        this.mBinding = viewDataBinding;
        //this.bindingInterface = bindingInterface;
    }

    //endregion

    //region Public Methods

    public void bind(Object obj, Object clickListener, int position) {
        mBinding.setVariable(BR.obj, obj);
        mBinding.setVariable(BR.clickListener, clickListener);

        View baseView = mBinding.getRoot().findViewById(R.id.baseLlt);

        if (baseView != null) baseView.setTag(position);

        mBinding.executePendingBindings();
    }

    //endregion

}
