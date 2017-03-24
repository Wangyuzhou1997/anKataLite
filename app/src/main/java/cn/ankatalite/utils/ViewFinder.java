package cn.ankatalite.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by per4j on 17/3/24.
 */

public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int viewId) {
        return mActivity == null ? mView.findViewById(viewId) : mActivity.findViewById(viewId);
    }
}
