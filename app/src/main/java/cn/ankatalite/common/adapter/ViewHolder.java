package cn.ankatalite.common.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by per4j on 17/3/25.
 */
public class ViewHolder {
    private View mConvertView;
    private SparseArray<WeakReference<View>> mViews;

    public ViewHolder(Context context, View convertView, int layoutId) {

        convertView = View.inflate(context, layoutId, null);
        convertView.setTag(this);

        this.mConvertView = convertView;

        mViews = new SparseArray<>();
    }

    public static ViewHolder getViewHolder(Context context, View convertView, int layoutId) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder(context, convertView, layoutId);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return viewHolder;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }

        return this;
    }

    public ViewHolder setImageRes(int viewId, int resId) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            iv.setImageResource(resId);
        }

        return this;
    }

    private <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }

        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, new WeakReference<View>(view));
        }

        return (T) view;
    }
}
