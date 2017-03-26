package cn.ankatalite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.ankatalite.db.DbHelper;
import cn.ankatalite.db.DbManager;
import cn.ankatalite.db.Person;
import cn.ankatalite.db_sdk.DbSupportFactory;
import cn.ankatalite.db_sdk.DefaultDbSupport;
import cn.ankatalite.db_sdk.IDbSupport;
import cn.ankatalite.db_sdk.Student;
import cn.ankatalite.utils.OnClick;
import cn.ankatalite.utils.ViewById;
import cn.ankatalite.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv)
    private TextView mTv;
    @ViewById(R.id.input_et)
    private EditText mInputEt;
    @ViewById(R.id.screen_tv)
    private TextView mScreenTv;
    private IDbSupport dbSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        dbSupport = DbSupportFactory.getFactory(this, Person.class, Student.class).getDbSupport(new DefaultDbSupport(), Person.class);
    }

    @OnClick(R.id.btn)
    private void click() {
        Toast.makeText(MainActivity.this, mTv.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.add)
    private void add() {
        /*Person person = new Person("dapan", new Random().nextInt(30));
        long insert = mDbManager.insert(person);
        if (insert != -1) {
            Toast.makeText(MainActivity.this, "插入成功: " + insert, Toast.LENGTH_SHORT).show();
        }*/
        String id = mScreenTv.getText().toString().trim();
        Person person;
        if (!TextUtils.isEmpty(id)) {
            person = new Person(Integer.valueOf(id) + 1, getRandomStr(), new Random().nextInt(30));
        } else {
            person = new Person(getRandomStr(), new Random().nextInt(30));
        }


        long insert = dbSupport.insert(person);
        if (insert != -1) {
            mScreenTv.setText(insert + "");
            Toast.makeText(MainActivity.this, "插入成功: " + insert, Toast.LENGTH_SHORT).show();
        }
    }

    private String getRandomStr() {
        List list = new ArrayList();
        for (char i='a'; i<='z'; i++) {
            list.add(i);
        }
        String result = "";
        for (int i=0; i<9; i++) {
            int pos = (int) (Math.random()*26);
            result += list.get(pos);
        }
        return result;
    }

    @OnClick(R.id.del)
    private void del() {
        /*int delete = mDbManager.delete("name=?", "dapan");
        Toast.makeText(MainActivity.this, "删除成功: " + delete, Toast.LENGTH_SHORT).show();*/
        int delete = dbSupport.delete("name=?", "abc");
        Toast.makeText(MainActivity.this, "删除成功: " + delete, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.update)
    private void update() {
        Person person = new Person("dapan", 29);
        int update = dbSupport.update(person, "id=?", "1");
        Toast.makeText(MainActivity.this, "更新成功: " + update, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.query)
    private void query() {
        List<Person> persons = dbSupport.getQuery(Person.class).queryAll();
        if (persons == null || persons.size() <=0) {
            Toast.makeText(MainActivity.this, "没有数据！ ", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Person person : persons) {
            sb.append(person.toString()).append("/n");
        }
        Toast.makeText(MainActivity.this, "person count: " + sb.toString(), Toast.LENGTH_SHORT).show();
    }
}
