package kioskmode.com.epoptia.adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import kioskmode.com.epoptia.R;

/**
 * Created by giannis on 5/9/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mBinding;
//    private RecyclerCallback bindingInterface;

    public RecyclerViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        this.mBinding = viewDataBinding;
//        this.bindingInterface = bindingInterface;
    }

    public void bind(Object obj, Object object, int position) {
        mBinding.setVariable(BR.obj, obj);
        mBinding.setVariable(BR.presenter, object);
        View baseView = mBinding.getRoot().findViewById(R.id.baseLlt);
        if (baseView != null) baseView.setTag(position);
        mBinding.executePendingBindings();
    }
}
