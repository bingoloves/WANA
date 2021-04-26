package cn.cqs.common.auto;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by bingo on 2021/3/24.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/24
 */

public class AutoBundle {

    private AutoBundle() {
        throw new AssertionError("no instances");
    }

    /**
     * assign to target fields from {@link Activity#getIntent()}
     * @param activity target activity which has {@link AutoBundleField} annotated fields.
     */
    public static void bind(@NonNull Activity activity) {
        bind(activity, activity.getIntent());
    }
    public static void bind(@NonNull Fragment fragment) {
        bind(fragment, fragment.getActivity() == null ? null : fragment.getActivity().getIntent());
    }

    /**
     * 自动注入Intent中数据到页面中
     * @param object
     */
    public static void bind(final Object object,Intent intent) {
        if (intent == null ) return;
        Class annotationParent = object.getClass();
        Field[] fields = annotationParent.getDeclaredFields();
        for (final Field field : fields) {
            String type = field.getGenericType().toString();
            AutoBundleField autoWiredAnnotation = field.getAnnotation(AutoBundleField.class);
            if (autoWiredAnnotation != null) {
                field.setAccessible(true);
                String name = autoWiredAnnotation.name();
                if (TextUtils.isEmpty(name)){
                    name = field.getName();
                }
                try {
                    Object defaultValue = field.get(object);
                    Object intentValue = getIntentValue(type,name,defaultValue,intent);
                    field.set(object, intentValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取对应类型的 数据值
     *
     * @param type
     * @param name
     * @param defaultValue
     * @param intent
     * @return
     */
    private static Object getIntentValue(String type, String name,Object defaultValue,Intent intent) {
        Object result = null;
        if (intent != null){
            if ("class java.lang.String".equals(type)){
                result = intent.getStringExtra(name);
            } else if ("class java.lang.Integer".equals(type) || "int".equals(type)){
                result = intent.getIntExtra(name, (Integer) defaultValue);
            } else if ("class java.lang.Double".equals(type) || "double".equals(type)){
                result = intent.getDoubleExtra(name, (Double) defaultValue);
            } else if ("class java.lang.Boolean".equals(type) || "boolean".equals(type)){
                result = intent.getBooleanExtra(name, (Boolean) defaultValue);
            }else if ("class java.lang.Float".equals(type) || "float".equals(type)){
                result = intent.getFloatExtra(name, (Float) defaultValue);
            } else if ("class java.lang.Long".equals(type) || "long".equals(type)){
                result = intent.getLongExtra(name, (Long) defaultValue);
            } else if ("class java.io.Serializable".equals(type)){
                result = intent.getSerializableExtra(name);
            } else if ("class android.os.Parcelable".equals(type)){
                result = intent.getParcelableExtra(name);
            } else if ("class android.os.Bundle".equals(type)){
                result = intent.getBundleExtra(name);
            } else {
                result = intent.getSerializableExtra(name);
            }
        }
        return result;
    }

    /**
     * 反射获取方法 返回值
     * @param bean
     * @param methodName
     * @param params
     * @param paramClass
     * @return
     */
    public static Object executeReflectMethod(Object bean,String methodName,Object[] params,Class<?> paramClass){
        Method serviceMethod  = null;Object result = null;
        if(null == paramClass){
            try {
                serviceMethod  = bean.getClass().getMethod(methodName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }else{
            try {
                serviceMethod = bean.getClass().getMethod(methodName,paramClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            result = serviceMethod.invoke(bean,params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
