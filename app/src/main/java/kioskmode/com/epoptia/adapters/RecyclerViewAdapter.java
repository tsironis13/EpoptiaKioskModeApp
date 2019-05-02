package kioskmode.com.epoptia.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by giannis on 5/9/2017.
 */

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    //region Private Properties

    private int layoutId;

    private ViewDataBinding mBinding;

    //endregion

    //region Constructor

    public RecyclerViewAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    //endregion

    //region Abstract Methods

    protected abstract Object getObjForPosition(int position, ViewDataBinding mBinding);

    protected abstract Object getClickListenerObject();

    protected abstract int getLayoutIdForPosition(int position);

    protected abstract int getTotalItems();

    //endregion

    //region Public Methods

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        mBinding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false);

        return new RecyclerViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Object item = getObjForPosition(position, mBinding);

        holder.bind(item, getClickListenerObject(), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return getTotalItems();
    }

    //endregion

}
