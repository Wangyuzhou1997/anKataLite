package cn.ankatalite.common.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by per4j on 17/3/27.
 */

public abstract class AbsFooterAdapter<T, V extends IAdapterView> extends ListAdapter<T, V> {

    protected static final int VIEW_ITEM_TYPE = 1; // 普通的item类型
    protected static final int VIEW_ITEM_FOOTER = 2; // 底部item类型

    public AbsFooterAdapter(Context context, List<T> mData) {
        super(context, mData);
    }

    public boolean isFooterEnabled() {
        return false;
    }

    protected View createFooterView() {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM_FOOTER) {
            return new RecyclerView.ViewHolder(createFooterView()) {};
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_ITEM_TYPE) {
            super.onBindViewHolder(holder, position);
        }
    } //Footer类型的不需要bindView()

    @Override
    public int getItemViewType(int position) { // 根据position返回ItemView的类型
        if (isFooterEnabled() && position == getItemCount() - 1) {
            return VIEW_ITEM_FOOTER;
        } //只有Footer可用，并且position是最后一项时，才返回VIEW_ITEM_FOOTER类型
        return VIEW_ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (isFooterEnabled()) {
            itemCount += 1;
        }
        return itemCount;
    } //footer可用，需要在原有个数的基础上+1

    @Override
    public T getItem(int position) {
        if (getItemViewType(position) == VIEW_ITEM_FOOTER) {
            return null;
        } //footer类型的Item，不需要返回数据
        return super.getItem(position);
    }
}
