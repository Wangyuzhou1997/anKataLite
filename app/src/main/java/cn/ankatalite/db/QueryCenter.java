package cn.ankatalite.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by per4j on 17/3/26.
 */

public class QueryCenter<T> {

    private SQLiteDatabase mDatabase;
    private Class<T> mClazz;

    private String[] columns;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public QueryCenter(SQLiteDatabase database, Class<T> clazz) {
        this.mDatabase = database;
        this.mClazz = clazz;
    }

    public List<T> queryAll() {
        String tableName = DbUtil.getTableName(mClazz);
        Cursor cursor = mDatabase.query(tableName, null, null, null, null, null, null);

        return cursor2List(cursor);
    }

    public List<T> query() {
        String tableName = DbUtil.getTableName(mClazz);
        Cursor cursor = mDatabase.query(tableName, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);

        clearParams();

        return cursor2List(cursor);
    }

    private List<T> cursor2List(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();
                        int columnIndex = cursor.getColumnIndex(name);
//                        cursor.getString(columnIndex);
                        if (columnIndex == -1) {
                            continue;
                        }

                        Method cursorMethod = cursorMethod(field.getType());
                        if (cursorMethod != null) {
                            Object value = cursorMethod.invoke(cursor, columnIndex);
                            if (value == null) {
                                continue;
                            }

                            value = DbUtil.parseTypeValue(field, value);

                            field.set(instance, value);
                        }
                        list.add(instance);
                    } // for end

                } while (cursor.moveToNext());

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private Method cursorMethod(Class<?> type) throws NoSuchMethodException {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getDeclaredMethod(methodName, int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName = null;
        if (fieldType.isPrimitive()) {
            typeName = DbUtil.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }


        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }

        return methodName;
    }


    public QueryCenter setColumns(String[] columns) {
        this.columns = columns;

        return this;
    }

    public QueryCenter setSelection(String selection) {
        this.selection = selection;

        return this;
    }

    public QueryCenter setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;

        return this;
    }

    public QueryCenter setGroupBy(String groupBy) {
        this.groupBy = groupBy;

        return this;
    }

    public QueryCenter setHaving(String having) {
        this.having = having;

        return this;
    }

    public QueryCenter setOrderBy(String orderBy) {
        this.orderBy = orderBy;

        return this;
    }

    public QueryCenter setLimit(String limit) {
        this.limit = limit;

        return this;
    }

    private void clearParams() {
        columns = null;
        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;
        limit = null;
    }
}
