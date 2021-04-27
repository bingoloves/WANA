package cn.cqs.wana.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.widget.BadgeView;
import cn.cqs.wana.R;

/**
 * Created by bingo on 2021/4/27.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/27
 */

public class WidgetActivity extends BaseActivity {
    @BindView(R.id.tv_badge)
    TextView badgeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        initBadgeView();

    }

    /**
     * 角标组件
     */
    private void initBadgeView() {
        BadgeView badge = new BadgeView(this, badgeTv);
        badge.setText("12");
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setBadgeMargin(0, 2);
        badge.setBadgeBackgroundColor(Color.RED);
        badgeTv.setOnClickListener(v -> {
//            TranslateAnimation translate = new TranslateAnimation(-100, 0, 0, 0);
//            translate.setInterpolator(new BounceInterpolator());
//            translate.setDuration(1000);
            AlphaAnimation alpha = new AlphaAnimation(0,1);
            alpha.setInterpolator(new BounceInterpolator());
            alpha.setDuration(1000);
            badge.toggle(alpha, null);
        });
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }

}
