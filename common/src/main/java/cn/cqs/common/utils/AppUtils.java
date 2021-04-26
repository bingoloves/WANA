package cn.cqs.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Method;

import cn.cqs.base.ActivityStackManager;
import cn.cqs.xlayout.AlphaTextView;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class AppUtils {

    private AppUtils utils;
    private Application mGlobalApplication;
    /**
     * 禁止外部创建 ServiceFactory 对象
     */
    private AppUtils() {
    }

    /**
     * 通过静态内部类方式实现 ServiceFactory 的单例
     */
    public static AppUtils getInstance() {
        return Inner.applicationUtils;
    }

    private static class Inner {
        private static AppUtils applicationUtils = new AppUtils();
    }

    /**
     * 可外部传入
     * @param application
     */
    public void init(Application application){
        this.mGlobalApplication = application;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStackManager.getStackManager().addActivity(activity);
                //applyGrayTheme(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStackManager.getStackManager().removeActivity(activity);
            }
        });
    }
    private Typeface typeface;

    /**
     * 这种方式需要在Activity的onCreate之前调用
     * @param activity
     * @param assetsName  如："hwxk.ttf"
     */
    public void applyFont(final Activity activity,String assetsName){
        if (typeface == null) {
            typeface = Typeface.createFromAsset(activity.getAssets(), assetsName);
        }
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        LayoutInflaterCompat.setFactory2(layoutInflater, new LayoutInflater.Factory2() {

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                View view = null;
                if ("cn.cqs.xlayout.AlphaTextView".equals(name)){
                    view = new AlphaTextView(context,attrs);
                    ((TextView) view).setTypeface(typeface);
                } else {
                    try {
                        AppCompatDelegate delegate = ((AppCompatActivity)activity).getDelegate();
                        view = delegate.createView(parent, name, context, attrs);
                        if (view!= null && view instanceof TextView){
                            ((TextView) view).setTypeface(typeface);
                        }
                    } catch (Exception e){
                       e.printStackTrace();
                    }
                }
                return view;
            }
        });
    }
    /**
     * 设置灰色主题（清明节日）
     * @param activity
     */
    private void applyGrayTheme(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.getWindow() != null
                && activity.getWindow().getDecorView() != null) {
            setViewGray(activity.getWindow().getDecorView());
        }
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (layoutInflater == null) {
            return;
        }
    }
    private void setViewGray(View view){
        Paint mPaint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        view.setLayerType(View.LAYER_TYPE_HARDWARE,mPaint);
    }
    /**
     * 获取当前的Application
     * @return
     */
    public Application getApplication(){
        if (mGlobalApplication == null){
            Application curApplication = getCurApplication();
            if (curApplication != null){
                mGlobalApplication = curApplication;
            }
        }
        return mGlobalApplication;
    }
    /**
     * 获取应用名称
     * @return
     */
    public String getAppName() {
        try {
            PackageManager packageManager = getApplication().getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(getApplication().getApplicationInfo()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取当前应用的Application
     * 先使用ActivityThread里获取Application的方法，如果没有获取到，
     * 再使用AppGlobals里面的获取Application的方法
     * @return
     */
    private static Application getCurApplication(){
        Application application = null;
        try{
            Class atClass = Class.forName("android.app.ActivityThread");
            Method currentApplicationMethod = atClass.getDeclaredMethod("currentApplication");
            currentApplicationMethod.setAccessible(true);
            application = (Application) currentApplicationMethod.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(application != null) return application;
        try{
            Class atClass = Class.forName("android.app.AppGlobals");
            Method currentApplicationMethod = atClass.getDeclaredMethod("getInitialApplication");
            currentApplicationMethod.setAccessible(true);
            application = (Application) currentApplicationMethod.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return application;
    }
}
