package cn.ankatalite.common.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by per4j on 17/3/25.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    private List<T> mList;
    private int mLayoutId;
    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mList = datas;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, mLayoutId);

        convert(viewHolder, position, mList.get(position));

        return viewHolder.getConvertView();
    }

    protected abstract void convert(ViewHolder viewHolder, int position, T data);
}
