package cn.ankatalite.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by per4j on 17/3/24.
 */

public class ViewUtils {

    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    public static void inject(ViewFinder viewFinder, Object object) {
        injectView(viewFinder, object);
        injectEvent(viewFinder, object);
    }

    private static void injectEvent(ViewFinder viewFinder, Object object) {
        // 1.获取类里面所用的方法
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            // 2.获取OnClick注解
            OnClick onClick = method.getAnnotation(OnClick.class);
            // 2.1 获取ViewById注解的value值
            int[] value = onClick.value();
            for (int id : value) {
                View view = viewFinder.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(new DeclaredOnClickListener(method, view));
                }
            }


        }
    }

    private static void injectView(ViewFinder viewFinder, Object object) {
        // 1.获取类里面所用的属性
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 2.获取ViewById注解
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                // 2.1 获取ViewById注解的value值
                int value = viewById.value();
                // 3.findViewById找到View
                View view = viewFinder.findViewById(value);
                if (view != null) {
                    // 4.动态注入找到的View
                    field.setAccessible(true);
                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        private Method mMethod;
        private Object mObject;

        public DeclaredOnClickListener(Method method, Object object) {
            this.mMethod = method;
            this.mObject = object;
        }

        @Override
        public void onClick(View v) {
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject, v);//注意，这里注入的方法，必须包含参数Viw v
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject);//如果发现方法没有参数，就会在这里被捕获，这时再注入一个无参的方法。
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
