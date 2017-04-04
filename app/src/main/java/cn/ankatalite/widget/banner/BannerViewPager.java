package cn.ankatalite.widget.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by per4j on 17/3/28.
 */

public class BannerViewPager extends ViewPager {

    private int mDuration = 1200;
    private BannerAdapter mAdapter;
    private BannerScroller mScroller;
    private List<View> mConvertViews;
    private Activity mActivity;

    private static final int SCROLL_MSG = 0x001;
    private static final int DELAYMILLIS = 3500;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL_MSG:
                    setCurrentItem(getCurrentItem()+1);

                    autoScroll();
                    break;
            }
        }
    };

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mActivity = (Activity) context;
        try {
            Field field = getClass().getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new BannerScroller(context);
            mScroller.setBannerScrollerDuration(mDuration);
            field.set(this, mScroller); //为当前类的mScroller赋新值
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mConvertViews = new ArrayList<>();
    }

    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;

        setCurrentItem(Integer.MAX_VALUE >> 2 * mAdapter.getCount());

        getResources();

        super.setAdapter(new BannerPagerAdapter());

        mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallback(){
        @Override
        public void onActivityResumed(Activity activity) {
            if (mActivity == activity) {
                autoScroll();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (mActivity == activity) {
                mHandler.removeCallbacksAndMessages(SCROLL_MSG);
            }
        }
    };

    public void setBannerScrollerDuration(int duration) {
        this.mDuration = duration;
    }

    public void autoScroll() {
        mHandler.removeCallbacksAndMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, DELAYMILLIS);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacksAndMessages(SCROLL_MSG);
        mHandler = null;

        mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        super.onDetachedFromWindow();
    }

    private class BannerPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mAdapter.getView(getConvertView(), position % mAdapter.getCount());
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }

        /**
         * 只有当View没有parent时，再重用，否则返回空
         */
        public View getConvertView() {
            if (mConvertViews != null && mConvertViews.size() >0) {
                for (View convertView : mConvertViews) {
                    if (convertView != null && convertView.getParent() == null) {
                        return convertView;
                    }
                }
            }
            return null;
        }
    }
}
