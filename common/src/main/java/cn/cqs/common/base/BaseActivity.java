package cn.cqs.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.gyf.immersionbar.ImmersionBar;
import java.lang.ref.WeakReference;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cqs.base.ActivityStackManager;
import cn.cqs.common.R;
import cn.cqs.common.auto.AutoBundle;
import cn.cqs.toast.ToastUtils;

public class BaseActivity extends AppCompatActivity {
    protected Unbinder unbinder;
    protected WeakReference<Activity> activityWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWeakReference = new WeakReference<Activity>(this);
        AutoBundle.bind(this);
    }
    protected Activity getActivity(){
        return activityWeakReference.get();
    }
    /**
     * 简化版
     * @param msg
     */
    protected void toast(String msg){
        ToastUtils.show(msg);
    }

    /**
     * 初始化沉浸式状态
     */
    protected void initImmersionbar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        initImmersionbar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        initImmersionbar();
    }
    /**
     * 获取颜色资源
     * @param color
     * @return
     */
    protected int getColorRes(@ColorRes int color){
        return getResources().getColor(color);
    }
    /**
     * 获取String资源
     * @param id
     * @return
     */
    protected String getStringRes(@StringRes int id){
        return getResources().getString(id);
    }

    /**
     * 关闭软键盘
     */
    protected void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     * @param et 输入焦点
     */
    protected void showKeyboard(EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
    }

    /**
     * 重定向，关闭其他全部页面
     */
    protected void redirectWithFinishAll(Class<? extends Activity> clazz){
        Intent intent = new Intent(getActivity(), clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        if (ActivityStackManager.getActivityStack().size() > 1){
            overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
        } else {
            overridePendingTransition(0,0);
        }
    }

    /**
     * 用于跳转后关闭自身，主要是直接finish影响转场动画
     */
    protected void delayFinish(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
