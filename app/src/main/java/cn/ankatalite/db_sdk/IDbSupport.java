package cn.ankatalite.db_sdk;

import cn.ankatalite.db.DbHelper;
import cn.ankatalite.db.QueryCenter;

/**
 * Created by per4j on 17/3/26.
 */

public interface IDbSupport<T> {

    long insert(T obj);

    int delete(String whereClause, String... whereArgs);

    int update(T obj, String whereClause, String... whereArgs);

    QueryCenter getQuery(Class<?> clazz);

    void setDbHelperAndClazz(DbHelper mDbHelper, Class<T> clazz);
}
