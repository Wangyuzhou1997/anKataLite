package cn.ankatalite.db_sdk;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import cn.ankatalite.db.DbHelper;
import cn.ankatalite.db.DbUtil;
import cn.ankatalite.db.Person;
import cn.ankatalite.db.QueryCenter;

/**
 * Created by per4j on 17/3/26.
 */

public class DefaultDbSupport<T> implements IDbSupport<T> {
    private DbHelper mDbHelper;
    private Class<T> mClazz;
    private QueryCenter mQueryCenter;


    private SQLiteDatabase getWritableDatabase() {
        return mDbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        return mDbHelper.getReadableDatabase();
    }

    @Override
    public long insert(T obj) {
        ContentValues values = contentValuesByTFinal(obj);
        SQLiteDatabase database = getWritableDatabase();
        long insert = database.insert(DbUtil.getTableName(obj.getClass()), null, values);
        return insert;
    }

    public void insert(List<T> list) {
        getWritableDatabase().beginTransaction();
        for (T obj : list) {
            insert(obj);
        }
        getWritableDatabase().setTransactionSuccessful();
        getWritableDatabase().endTransaction();
    }

    @Override
    public int delete(String whereClause, String... whereArgs) {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(DbUtil.getTableName(mClazz), whereClause, whereArgs);

        return delete;
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = contentValuesByTFinal(obj);
        int update = database.update(DbUtil.getTableName(obj.getClass()), values, whereClause, whereArgs);

        return update;
    }

    private static final Object[] sPutMethodArgs = new Object[2]; // 缓存，避免不必要的创建
    private static final Map<String, Method> sPutMethod = new ArrayMap<String, Method>(); //缓存方法

    private <T> ContentValues contentValuesByTFinal(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();

                //name = $change, type = IncrementalChange
                if (field.isSynthetic() || "serialVersionUID".equals(name)) {
                    continue;
                }
                Object value = field.get(obj);
                sPutMethodArgs[0] = name;
                sPutMethodArgs[1] = value;

                String typeName = field.getType().getSimpleName();
                Method putMethod = sPutMethod.get(typeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    sPutMethod.put(typeName, putMethod);
                }

                //通过反射，执行方法，就像：values.put(name, value);
                if (name.equals("id") && Integer.valueOf(String.valueOf(value)) == 0) { //TODO:测试使用
                    continue;
                }
                putMethod.invoke(values, sPutMethodArgs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sPutMethodArgs[0] = null;
            sPutMethodArgs[1] = null;
        }

        return values;
    }

    @Override
    public QueryCenter getQuery(Class<?> clazz) {
        if (mQueryCenter == null) {
            mQueryCenter = new QueryCenter(getReadableDatabase(), clazz);
        }
        return mQueryCenter;
    }

    public void setDbHelperAndClazz(DbHelper dbHelper, Class<T> clazz) {
        this.mDbHelper = dbHelper;
        this.mClazz = clazz;
    }
}
