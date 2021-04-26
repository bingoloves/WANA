package cn.cqs.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.cqs.base.DensityUtils;

/**
 * Created by bingo on 2021/4/19.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/19
 */

public class SpaceHeaderView extends LinearLayout{
    private Context context;
    public SpaceHeaderView(Context context) {
        super(context);
        initView(context);
    }
    public SpaceHeaderView(Context context, int height){
        this(context);
        setHeight(height);
    }
    public SpaceHeaderView(Context context, int height, int color){
        this(context);
        setHeight(height);
        setColor(color);
    }
    public SpaceHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private void initView(Context context) {
        this.context = context;
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 设置高度
     * @param height
     */
    public void setHeight(int height){
        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = DensityUtils.dp2px(context,height);
        setLayoutParams(layoutParams);
    }
    /**
     * 设置宽度
     * @param width
     */
    public void setWidth(int width){
        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = DensityUtils.dp2px(context,width);
        setLayoutParams(layoutParams);
    }
    public void setColor(int color){
        setBackgroundColor(getResources().getColor(color));
    }
}
