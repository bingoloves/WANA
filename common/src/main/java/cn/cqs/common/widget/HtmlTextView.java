package cn.cqs.common.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;

/**
 * Created by bingo on 2021/4/15.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/15
 */

public class HtmlTextView extends AppCompatTextView {

    public HtmlTextView(Context context) {
        super(context);
        setText(fromHtml(getText().toString()));
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText(fromHtml(getText().toString()));
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setText(fromHtml(getText().toString()));
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}