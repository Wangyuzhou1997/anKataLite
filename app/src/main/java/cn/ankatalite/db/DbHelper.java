package cn.ankatalite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by per4j on 17/3/25.
 */

public class DbHelper<T> extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "person.db";

    private T[] array;
    public static final String  TABLE_NAME = "person";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME +
            " (id integer primary key autoincrement, name text, age integer);";

    public DbHelper(Context context, T...obj) {
        super(context, DB_NAME, null, VERSION);
        array = obj;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_SQL);
        for (T t : array) {
            String createTableSql = DbUtil.generatorCreateTableSql(t.getClass());
            Log.e("TAG", "createTableSql:" + createTableSql);
            db.execSQL(createTableSql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
