package cn.ankatalite.widget.banner;

import android.view.View;

/**
 * Created by per4j on 17/3/29.
 */

public abstract class BannerAdapter {
    public abstract View getView(View convertView, int position);

    public abstract int getCount();

    public String getBannerDesc(int position) {
        return null;
    }
}
