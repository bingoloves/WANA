package cn.cqs.wana.app.main;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;

import cn.cqs.common.base.BaseActivity;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }
}
