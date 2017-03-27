package cn.ankatalite.common.md.adapter;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by per4j on 17/3/27.
 */

public class LogUtil {
    public static void e(String tag, String msg) {
//        Logger.e(tag, msg);
        Log.e(tag, msg);
    }
}
