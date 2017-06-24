package me.ghui.v2er.adapter.base;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.List;

import me.ghui.v2er.general.PreConditions;
import me.ghui.v2er.util.Utils;

/**
 * Created by zhy on 16/4/9.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerView.LayoutManager mLayoutManager;

    public MultiItemTypeAdapter(Context context) {
        mContext = context;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public void setData(List<T> data, boolean shouldAppend) {
        if (shouldAppend) {
            mDatas.addAll(data);
            notifyItemRangeChanged(mDatas.size() - data.size(), data.size());
        } else {
            mDatas = data;
            notifyDataSetChanged();
        }
    }

    public void setData(List<T> data) {
        setData(data, false);
    }


    public T getItem(int position) {
        return PreConditions.isEmpty(mDatas) ? null : mDatas.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mLayoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(getItem(position), position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder;
        if (itemViewDelegate instanceof ItemViewDelegateAdapter && ((ItemViewDelegateAdapter) itemViewDelegate).getItemView() != null) {
            holder = ViewHolder.createViewHolder(mContext, ((ItemViewDelegateAdapter) itemViewDelegate).getItemView());
        } else {
            holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        }
        onViewHolderCreated(holder, holder.getConvertView());
        bindListener(holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    @CallSuper
    protected void bindListener(ViewHolder holder, int viewType) {
        if (!isEnabled(viewType)) return;
        holder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, holder, holder.getAdapterPosition());
            }
        });

        holder.getConvertView().setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                return mOnItemLongClickListener.onItemLongClick(v, holder, holder.getAdapterPosition());
            }
            return false;
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, getItem(position));
        animate(holder.itemView, position);
    }

    private void animate(View itemView, int position) {
        if (itemView.getAnimation() != null) {
            itemView.clearAnimation();
        }
        if (mLayoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutmanager = (LinearLayoutManager) mLayoutManager;
            int firstVisiableItem = layoutmanager.findFirstVisibleItemPosition();
            int lastVisiableItem = layoutmanager.findLastVisibleItemPosition();
            Logger.d("position: " + position + ", first: " + firstVisiableItem + ", last: " + lastVisiableItem);
            if (position > lastVisiableItem) {
                animateIn(itemView);
            } else if (position < firstVisiableItem) {
                animateOut(itemView);
            }
        }
    }


    protected void animateIn(View itemView) {
    }

    protected void animateOut(View itemView) {
    }


    @Override
    public int getItemCount() {
        return Utils.listSize(mDatas);
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

}
