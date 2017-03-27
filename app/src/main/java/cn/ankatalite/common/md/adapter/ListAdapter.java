package cn.ankatalite.common.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by per4j on 17/3/27.
 */

public abstract class ListAdapter<T, V extends IAdapterView> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> mData;
    private int mLastItemClickPosition = RecyclerView.NO_POSITION;

    public ListAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    public ListAdapter(Context context, List<T> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    protected abstract V createView(Context context);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = (View) createView(mContext);
        final RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(itemView) { };
        //添加点击事件
        if (mItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mLastItemClickPosition = position;
                        mItemClickListener.onItemClick(position);
                    }
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IAdapterView itemView = (IAdapterView) holder.itemView;
        itemView.bind(getItem(position), position); //子类在IAdapterView的bind()方法绑定Item View
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void addData(List<T> data) {
        if (mData ==null) {
            mData = data;
        } else {
            mData.addAll(data);
        }
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    public int getLastItemClickPosition() {
        return mLastItemClickPosition;
    }

    private OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
