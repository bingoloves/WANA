package cn.cqs.common.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gyf.immersionbar.ImmersionBar;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cqs.common.auto.AutoBundle;
import cn.cqs.toast.ToastUtils;

/**
 * Created by bingo on 2020/11/24.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/24
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    protected View mContentView;
    //是否初始化完成
    protected boolean isInit = false;

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected void initData(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if( mContentView == null ){
            mContentView = inflater.inflate(getLayoutId(), container, false);
            unbinder= ButterKnife.bind(this, mContentView);
            AutoBundle.bind(this);
            initImmersionbar();
            initView();
            isInit = true;
        }
        return mContentView;
    }
    /**
     * 初始化沉浸式状态
     */
    protected void initImmersionbar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
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
     * 简化吐司
     * @param msg
     */
    protected void toast(CharSequence msg){
        ToastUtils.show(msg);
    }

    /**
     * 创建fragment的静态方法，方便传递参数
     * @param args 传递的参数
     * @return
     */
    public static Fragment newInstance(Class clazz, Bundle args) {
        Fragment mFragment = null;
        try {
            mFragment= (Fragment) clazz.newInstance();
            mFragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isInit){
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null){
            unbinder.unbind();
        }
    }
}
