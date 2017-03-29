package cn.ankatalite.widget.banner;

import android.content.Context;
import android.widget.Scroller;

/**
 * Created by per4j on 17/3/29.
 */

public class BannerScroller extends Scroller {
    private int mBannerScrollerDuration; //google不给提供设置的方法，我们自己动手丰衣足食~
    public BannerScroller(Context context) {
        super(context);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mBannerScrollerDuration);
    }

    public void setBannerScrollerDuration(int duration) {
        mBannerScrollerDuration = duration;
    }
}
