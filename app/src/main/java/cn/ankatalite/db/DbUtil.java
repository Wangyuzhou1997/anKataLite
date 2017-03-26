package cn.ankatalite.db;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by per4j on 17/3/26.
 */

public class DbUtil {

    private static Map<Class<?>, String> tableMap = new ArrayMap<>();

    private DbUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static <T> String getTableName(Class<?> clazz) {
        String tableName = tableMap.get(clazz);
        if (TextUtils.isEmpty(tableName)) {
            tableName = clazz.getSimpleName();
            Log.d("TAG", "第一次获取表名:" + tableName + ", obj:" + clazz);
            tableMap.put(clazz, tableName);
        }
        return tableName;
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        } else {
            value = " " + type;
        }
        return value;
    }

    /**
     * "CREATE TABLE IF NOT EXISTS person (id integer primary key autoincrement, name text, age integer);";
     */
    public static <T> String generatorCreateTableSql(Class<T> clazz) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(getTableName(clazz));
        sql.append(" (id integer primary key autoincrement, ");
        //获取属性，合成表中的必要字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();

            //name = $change, type = IncrementalChange
            if (field.isSynthetic() || "serialVersionUID".equals(name) || "id".equals(name)) {
                continue;
            }
            sql.append(name).append(getColumnType(type)).append(", ");
        }
        sql.replace(sql.length() - 2, sql.length(), ");"); //用");"替掉最后一个", "

        return sql.toString();
    }


    public static String capitalize(String name) {
        if (!TextUtils.isEmpty(name)) {
            return name.substring(0, 1).toUpperCase(Locale.US) + name.substring(1);
        }
        return null;
    }

    public static Object parseTypeValue(Field field, Object value) {
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            if ("0".equals(String.valueOf(value))) {
                value = false;
            } else if ("1".equals(String.valueOf(value))){
                value = true;
            }
        } else if (field.getType() == char.class || field.getType() == Character.class) {
            value = String.valueOf(value).charAt(0);
        } else if (field.getType() == Date.class) {
            Long date = (Long) value;
            if (date <= 0) {
                value = null;
            } else {
                value = new Date(date);
            }
        }
        return value;
    }
}
