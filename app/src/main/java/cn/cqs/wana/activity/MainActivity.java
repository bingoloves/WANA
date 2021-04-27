package cn.cqs.wana.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.cqs.base.DensityUtils;
import cn.cqs.common.adapter.QuickAdapter;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.bean.CommonItem;
import cn.cqs.common.utils.SpacesItemDecoration;
import cn.cqs.wana.R;

public class MainActivity extends BaseActivity implements LifecycleObserver{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    QuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        result.add(new CommonItem("VirtualApk", "它是滴滴实现的一种插件框架", item -> startActivity(new Intent(getActivity(),VirtualApkActivity.class))));
        result.add(new CommonItem("Widget", "部分自定义View", item -> startActivity(new Intent(getActivity(),WidgetActivity.class))));
        result.add(new CommonItem("xxx", "xxxxxx", item -> {
        }));
        result.add(new CommonItem("xxx", "xxxxxx", item -> {
        }));
        result.add(new CommonItem("xxx", "xxxxxx", item -> {
        }));
        result.add(new CommonItem("xxx", "xxxxxx", item -> {
        }));
        result.add(new CommonItem("xxx", "xxxxxx", item -> {
        }));
        result.add(new CommonItem("xxx", "xxxxxx", item -> {
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
            }
        };
        adapter.addHeadFootSpace(12);
        adapter.attachRecyclerView(recyclerView);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this,SpacesItemDecoration.VERTICAL);
        itemDecoration.setParam(R.color.color_F6F8FC, DensityUtils.dp2px(this,12));
        recyclerView.addItemDecoration(itemDecoration);
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
}
