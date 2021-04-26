package cn.cqs.base;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by bingo on 2021/4/20.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/20
 */

public class TextViewUtils {
    /**
     * 取消加粗
     * @param textView
     */
    public static void cancelBoldStyle(TextView textView){
        textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.NORMAL), Typeface.NORMAL);
        textView.invalidate();
    }
    /**
     * 设置加粗
     * @param textView
     */
    public static void setBoldStyle(TextView textView){
        textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.BOLD), Typeface.BOLD);
        textView.invalidate();
    }
}
