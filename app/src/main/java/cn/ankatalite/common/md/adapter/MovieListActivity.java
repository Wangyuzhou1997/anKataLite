package cn.ankatalite.common.md.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import cn.ankatalite.R;
import cn.ankatalite.common.md.adapter.model.JsonRootBean;
import cn.ankatalite.common.md.adapter.model.Subjects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by per4j on 17/3/27.
 */

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);

        mMovieRecyclerView = ((RecyclerView) findViewById(R.id.movie_list_recycler_view));
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMovieAdapter = new MovieAdapter(this, null);
        mMovieRecyclerView.setAdapter(mMovieAdapter);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder.url("http://api.douban.com/v2/movie/in_theaters")
                .get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                JsonRootBean jsonRootBean = gson.fromJson(result, JsonRootBean.class);
                final List<Subjects> subjects = jsonRootBean.getSubjects();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMovieAdapter.addData(subjects);
                        mMovieAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
