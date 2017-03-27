package cn.ankatalite.common.md.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ankatalite.common.md.adapter.model.Subjects;

/**
 * Created by per4j on 17/3/27.
 */

public class MovieAdapter extends AbsFooterAdapter<Subjects, DoubanItemView> {

    private Context mContext;
    private TextView mFooterTv;

    public MovieAdapter(Context context, List<Subjects> mData) {
        super(context, mData);
        this.mContext = context;
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateFooterData();
            }
        });
    }

    @Override
    public boolean isFooterEnabled() {
        return true;
    }

    @Override
    protected View createFooterView() {
        if (mFooterTv == null) {
            mFooterTv = new TextView(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
            mFooterTv.setLayoutParams(lp);
            mFooterTv.setVisibility(View.GONE);
            mFooterTv.setGravity(Gravity.CENTER);
        }
        updateFooterData();
        return mFooterTv;
    }

    private void updateFooterData() {
        int itemCount = getItemCount() - 1;
        if (itemCount > 0) {
            mFooterTv.setVisibility(View.VISIBLE);
            mFooterTv.setText("一共 " + itemCount + " 条数据");
        } else {
            mFooterTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected DoubanItemView createView(Context context) {
        return new DoubanItemView(context);
    }
}
