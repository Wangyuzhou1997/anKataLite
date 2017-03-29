package cn.ankatalite.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ankatalite.R;
import cn.ankatalite.utils.ViewById;
import cn.ankatalite.utils.ViewUtils;

/**
 * Created by per4j on 17/3/29.
 */

public class BannerView extends RelativeLayout {

    private final Context mContext;
    @ViewById(R.id.banner_vp)
    private BannerViewPager mBannerVp;
    @ViewById(R.id.banner_desc_tv)
    private TextView mBannerDescTv;
    @ViewById(R.id.banner_dot_indicator)
    private LinearLayout mBannerDotIndicator;
    @ViewById(R.id.banner_indicator_container)
    private RelativeLayout mBannerIndicatorContainer;
    private BannerAdapter mBannerAdapter;
    private Drawable mIndicatorNormal;
    private Drawable mIndicatorFocus;
    private int mDotSize = 8;
    private int mDotDistance = 10;
    private int mGravity;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context, R.layout.banner_view_layout, this); // 把banner_view_layout 填充到this中。

        ViewUtils.inject(this);

        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView);

        mDotSize = (int) a.getDimension(R.styleable.BannerView_dot_size, dip2px(mDotSize));
        mDotDistance = (int) a.getDimension(R.styleable.BannerView_dot_distance, dip2px(mDotDistance));
        mGravity = a.getInt(R.styleable.BannerView_gravity, mGravity);

        mIndicatorNormal = a.getDrawable(R.styleable.BannerView_dot_normal);
        if (mIndicatorNormal == null) {
            mIndicatorNormal = new ColorDrawable(Color.BLACK);
        }

        mIndicatorFocus = a.getDrawable(R.styleable.BannerView_dot_focus);
        if (mIndicatorFocus == null) {
            mIndicatorFocus = new ColorDrawable(Color.RED);
        }

        a.recycle();
    }

    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        mBannerVp.setAdapter(adapter);
        mBannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                pageSelect(position % mBannerAdapter.getCount());
            }
        });
        initIndicator();
    }

    private int mCurrentPosition = 0;

    /**
     * 需要考虑调用时机，因为要与ViewPager联动，但又不需要那么精细，所以直接在onPageSelected()方法中实现该功能即可。
     *
     * 切换原则是：1. 先将mCurrentPosition(默认为0)对应的View取出，设置为Normal状态，
     *           2. 然后更新mCurrentPosition
     *           3. 用最新的mCurrentPosition取对应的View，设置为Focus状态
     * @param position
     */
    private void pageSelect(int position) {
        if (mBannerDotIndicator != null) {
            IndicatorView oldView = (IndicatorView) mBannerDotIndicator.getChildAt(mCurrentPosition);
            oldView.setDrawable(mIndicatorNormal);

            mCurrentPosition = position;

            IndicatorView newView = (IndicatorView) mBannerDotIndicator.getChildAt(mCurrentPosition);
            newView.setDrawable(mIndicatorFocus);
        }
    }

    private void initIndicator() {
        int count = mBannerAdapter.getCount();
        mBannerDotIndicator.setGravity(getGravity(mGravity));
        for (int i = 0; i < count; i++) {
            IndicatorView indicatorView = new IndicatorView(mContext);
            if (i == 0) {
                indicatorView.setDrawable(mIndicatorFocus);
            } else {
                indicatorView.setDrawable(mIndicatorNormal);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            lp.leftMargin = mDotDistance;
            indicatorView.setLayoutParams(lp);
            mBannerDotIndicator.addView(indicatorView);
        }
    }

    private int getGravity(int gravity) {

        switch (gravity) {
            case 0:
                return Gravity.LEFT;

            case 1:
                return Gravity.CENTER;

            case -1:
                return Gravity.RIGHT;

            default:
                return Gravity.RIGHT;
        }
    }

    private int dip2px(int dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
                mContext.getResources().getDisplayMetrics());
    }

    public void autoScroll() {
        mBannerVp.autoScroll();
    }
}
