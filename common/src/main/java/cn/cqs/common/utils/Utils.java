package cn.cqs.common.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * 工具类
 */
public class Utils {
    private Utils(){
        throw new UnsupportedOperationException("Can not be instantiated.");
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static Drawable getDrawable(View view, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return view.getResources().getDrawable(id, view.getContext().getTheme());
        } else {
            return view.getResources().getDrawable(id);
        }
    }

    public static int getColor(View view, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return view.getResources().getColor(id, view.getContext().getTheme());
        } else {
            return view.getResources().getColor(id);
        }
    }
}
