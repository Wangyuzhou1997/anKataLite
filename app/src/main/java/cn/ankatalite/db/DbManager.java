package cn.ankatalite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by per4j on 17/3/25.
 */

public class DbManager {

    private static DbManager sDbManager;
    private DbHelper mDbHelper;

    private DbManager(Context context) {
        mDbHelper = new DbHelper(context);
    }

    public static DbManager getInstance(Context context) {
        if (sDbManager == null) {
            synchronized (DbManager.class) {
                if (sDbManager == null) {
                    sDbManager = new DbManager(context);
                }
            }
        }
        return sDbManager;
    }


    public long insert(Person person) {
        ContentValues values = contentValuesByT(person);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long insert = database.insert(DbHelper.TABLE_NAME, null, values);

        return insert;
    }

    public void insert(List<Person> list) {
        mDbHelper.getWritableDatabase().beginTransaction();
        for (Person obj : list) {
            insert(obj);
        }
        mDbHelper.getWritableDatabase().setTransactionSuccessful();
        mDbHelper.getWritableDatabase().endTransaction();
    }

    public int delete(String whereClause, String... whereArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int delete = database.delete(DbHelper.TABLE_NAME, whereClause, whereArgs);

        return delete;
    }

    public int update(Person person, String whereClause, String... whereArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        ContentValues values = contentValuesByT(person);
        int update = database.update(DbHelper.TABLE_NAME, values, whereClause, whereArgs);

        return update;
    }

    public List<Person> queryAll() {
        return query(null, null, null, null, null, null, null);
    }

    public List<Person> query(String[] columns,
                              String selection, String[] selectionArgs, String groupBy,
                              String having, String orderBy, String limit) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Cursor cursor = database.query(DbHelper.TABLE_NAME, columns,
                selection, selectionArgs, groupBy, having, orderBy, limit);

        if (cursor != null && cursor.moveToFirst()) {
            List<Person> persons = new ArrayList<>();
            do {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nameColumnIndex = cursor.getColumnIndex("name");
                int ageColumnIndex = cursor.getColumnIndex("age");
                int id = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                int age = cursor.getInt(ageColumnIndex);
                Person person = new Person(id, name, age);
                persons.add(person);

            } while (cursor.moveToNext());
            return persons;
        }
        return null;
    }

    private ContentValues contentValuesByObj(Person person) {
        ContentValues values = new ContentValues();
        if (person.getId() != 0) {
            values.put("id", person.getId());
        }
        values.put("name", person.getName());
        values.put("age", person.getAge());

        return values;
    }

    private <T> ContentValues contentValuesByT(T obj) {
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
                Object value = field.get(name);

                // 利用反射，获取ContentValues中的put方法，
                // getDeclaredMethod:
                // 第一个参数：方法名
                // 第二个参数：put方法的第一个参数
                // 第三个参数：put方法的第二个参数
                Method putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());

                //通过反射，执行方法，就像：values.put(name, value);
                putMethod.invoke(values, name, value);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return values;
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
                Object value = field.get(name);
                sPutMethodArgs[0] = name;
                sPutMethodArgs[1] = value;

                String typeName = field.getType().getSimpleName();
                Method putMethod = sPutMethod.get(typeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    sPutMethod.put(typeName, putMethod);
                }

                //通过反射，执行方法，就像：values.put(name, value);
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
}
