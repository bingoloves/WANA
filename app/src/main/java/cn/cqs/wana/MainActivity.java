package cn.cqs.wana;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cqs.common.adapter.QuickAdapter;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.bean.CommonItem;

public class MainActivity extends BaseActivity implements LifecycleObserver{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    QuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(this);
        initAdapter();
        initRefreshLayout();
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }

    private List<CommonItem> getAdapterListData() {
        List<CommonItem> result = new ArrayList<>();
        result.add(new CommonItem("IconicsDrawable", "点击改变字体图标", item -> {
            item.drawable = getRightIcon().color(Color.GREEN);
            adapter.notifyItemChanged(0);
        }));
        result.add(new CommonItem("DroidPlugin", "它是360手机助手实现的一种插件框架它可以在无需安装、修改的情况下运行APK文件", item -> {
            startActivity(new Intent(getActivity(),PluginActivity.class));
        }));
        return result;
    }

    private void initAdapter(){
        adapter = new QuickAdapter<CommonItem>(this,R.layout.list_item_common,getAdapterListData()){
            @Override
            protected void convert(@NonNull BaseViewHolder helper, CommonItem item) {
                super.convert(helper, item);
                helper.setText(R.id.tv_name,item.name);
                helper.setText(R.id.tv_content,item.content);
                helper.setImageDrawable(R.id.iv_arrow,item.drawable == null?getRightIcon():item.drawable);
            }
        };
        adapter.attachRecyclerView(recyclerView);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            CommonItem commonItem = (CommonItem) adapter.getData().get(position);
            if (commonItem.onItemClickListener != null) commonItem.onItemClickListener.onClick(commonItem);
        });
    }
    private void initRefreshLayout(){
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(layout -> layout.finishRefresh(1000));
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onActivityCreateEvent(){
        Log.e("TAG","ON_CREATE");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onActivityResumeEvent(){
        Log.e("TAG","ON_RESUME");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onActivityDestoryEvent(){
        Log.e("TAG","ON_DESTROY");
    }


    /**
     * 右侧图标
     * @return
     */
    private IconicsDrawable getRightIcon(){
        return new IconicsDrawable(this)
                .color(Color.RED)
                .sizeDp(10)
                .icon(Ionicons.Icon.ion_ios_arrow_right);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
//    }
}
