package cn.ankatalite.widget.banner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import cn.ankatalite.R;
import cn.ankatalite.utils.ViewById;
import cn.ankatalite.utils.ViewUtils;

/**
 * Created by per4j on 17/3/29.
 */

public class BannerActivity extends AppCompatActivity {

    @ViewById(R.id.banner_view)
    private BannerView mBannerView;
    private static final String[] imgs = {"http://img03.sogoucdn.com/app/a/100520020/e70e7be262b23fea01771a4b4da88591",
            "http://img02.sogoucdn.com/app/a/100520020/3d1e76b781651feff581a6155a0cb846",
            "http://img02.sogoucdn.com/app/a/100520024/7ecf23ae9580fd673ca798988bf7929d",
            "http://img04.sogoucdn.com/app/a/100520020/f3dd6647de038dcc478aa7945b2a9af8"};

    private static final String[] titles = {"这里是描述信息 title 1", "这里是描述信息 title 2",
            "这里是描述信息 title 3", "这里是描述信息 title 4"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banner);
        ViewUtils.inject(this);

        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                Logger.d(imgs[position]);
                ImageView iv = new ImageView(BannerActivity.this);
                Glide.with(BannerActivity.this).load(imgs[position]).placeholder(R.mipmap.ic_launcher).into(iv);
                return iv;
            }

            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public String getBannerDesc(int position) {
                return titles[position];
            }

        });
        mBannerView.autoScroll();
    }
}
