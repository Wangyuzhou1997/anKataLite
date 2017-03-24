package cn.ankatalite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import cn.ankatalite.utils.OnClick;
import cn.ankatalite.utils.ViewById;
import cn.ankatalite.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv)
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
    }


    @OnClick(R.id.btn)
    private void click() {
        Toast.makeText(MainActivity.this, mTv.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
