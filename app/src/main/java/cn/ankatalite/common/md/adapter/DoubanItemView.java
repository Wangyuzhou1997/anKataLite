package cn.ankatalite.common.md.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.ankatalite.R;
import cn.ankatalite.common.md.adapter.model.Casts;
import cn.ankatalite.common.md.adapter.model.Subjects;

/**
 * Created by per4j on 17/3/27.
 */

public class DoubanItemView extends RelativeLayout implements IAdapterView<Subjects> {

    private ImageView mMovieIv;
    private TextView mMovieCastTv;
    private TextView mMovieTitleTv;
    private TextView mMovieTypeTv;

    public DoubanItemView(Context context) {
        super(context);
        inflate(context, R.layout.movie_item, this);
    }

    @Override
    public void bind(Subjects item, int position) {
        if (mMovieIv == null) {
            mMovieIv = (ImageView) findViewById(R.id.movie_iv);
        }
        mMovieIv.setImageResource(R.mipmap.ic_launcher);
        if (mMovieCastTv == null) {
            mMovieCastTv = (TextView) findViewById(R.id.movie_cast_tv);
        }
        List<Casts> casts = item.getCasts();
        StringBuilder sb = new StringBuilder();
        for (Casts cast : casts) {
            sb.append(cast.getName()).append(", ");
        }
        mMovieCastTv.setText(sb.subSequence(0, sb.lastIndexOf(", ")));
        if (mMovieTitleTv == null) {
            mMovieTitleTv = (TextView) findViewById(R.id.movie_title_tv);
        }
        mMovieTitleTv.setText(item.getTitle());
        if (mMovieTypeTv == null) {
            mMovieTypeTv = (TextView) findViewById(R.id.movie_type_tv);
        }
        List<String> itemGenres = item.getGenres();
        sb = new StringBuilder();
        for (String itemGenre : itemGenres) {
            sb.append(itemGenre).append(", ");
        }
        mMovieTypeTv.setText(item.getGenres().toString());
    }
}
