package cn.cqs.plugin_login;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.OnClick;
import cn.cqs.common.base.BaseActivity;
@Route(path = "/login/main")
public class LoginActivity extends BaseActivity {

    @OnClick(R.id.test_btn)
    public void clickEvent(){
        toast("哈哈");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }
}
