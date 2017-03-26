package cn.ankatalite.db_sdk;

import android.content.Context;

import cn.ankatalite.db.DbHelper;
import cn.ankatalite.db.Person;

/**
 * Created by per4j on 17/3/26.
 */

public class DbSupportFactory<T> {

    private static DbSupportFactory sDbSupportFactory;
    private DbHelper mDbHelper;

    private DbSupportFactory(Context context, T...obj) {
        mDbHelper = new DbHelper(context, obj);
    }

    public static <T> DbSupportFactory getFactory(Context context, T...obj) {
        if (sDbSupportFactory == null) {
            synchronized (DbSupportFactory.class) {
                if (sDbSupportFactory == null) {
                    sDbSupportFactory = new DbSupportFactory(context);
                }
            }
        }
        return sDbSupportFactory;
    }

    public <T> IDbSupport getDbSupport(IDbSupport dbSupport, Class<T> clazz) {
        dbSupport.setDbHelperAndClazz(mDbHelper, clazz);
        return dbSupport;
    }
}
