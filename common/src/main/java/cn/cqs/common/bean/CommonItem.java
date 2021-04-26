package cn.cqs.common.bean;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by bingo on 2021/4/25.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/25
 */

public class CommonItem {
    public String name;
    public String content;
    public int icon;
    public Drawable drawable;
    public boolean bottomLine;
    public boolean arrow;
    //用于分段显示
    public int marginTop;
    public int marginBottom;
    public int marginLeft;
    public int marginRight;
    public OnItemClickListener onItemClickListener;

    public CommonItem(String name, String content, OnItemClickListener onItemClickListener) {
        this.name = name;
        this.content = content;
        this.onItemClickListener = onItemClickListener;
    }

    public CommonItem(String name, String content,int icon, boolean arrow, OnItemClickListener onItemClickListener) {
        this.name = name;
        this.content = content;
        this.icon = icon;
        this.arrow = arrow;
        this.onItemClickListener = onItemClickListener;
    }
    public CommonItem(String name, String content,Drawable drawable, OnItemClickListener onItemClickListener) {
        this.name = name;
        this.content = content;
        this.drawable = drawable;
        this.onItemClickListener = onItemClickListener;
    }

    public CommonItem(String name, String content, boolean bottomLine, boolean arrow, OnItemClickListener onItemClickListener) {
        this.name = name;
        this.content = content;
        this.bottomLine = bottomLine;
        this.arrow = arrow;
        this.onItemClickListener = onItemClickListener;
    }

    public CommonItem(String name, String content, int icon, boolean bottomLine, boolean arrow, int marginTop, int marginBottom, int marginLeft, int marginRight, OnItemClickListener onItemClickListener) {
        this.name = name;
        this.content = content;
        this.icon = icon;
        this.bottomLine = bottomLine;
        this.arrow = arrow;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(CommonItem item);
    }

}
