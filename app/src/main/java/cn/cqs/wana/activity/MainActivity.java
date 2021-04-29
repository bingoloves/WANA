package cn.cqs.wana.activity;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cqs.base.DensityUtils;
import cn.cqs.common.adapter.QuickAdapter;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.bean.CommonItem;
import cn.cqs.common.placeholder.Broccoli;
import cn.cqs.common.placeholder.BroccoliGradientDrawable;
import cn.cqs.common.placeholder.PlaceholderParameter;
import cn.cqs.common.utils.SpacesItemDecoration;
import cn.cqs.wana.R;

public class MainActivity extends BaseActivity implements LifecycleObserver{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    QuickAdapter adapter;
    private Map<View, Broccoli> mPlaceholderManager = new HashMap<>();
    private Map<View, Runnable> mTaskManager = new HashMap<>();
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
        result.add(new CommonItem("PhotoSelector", "图片选择框架", item -> startActivity(new Intent(getActivity(),PhotoSelectActivity.class))));
        result.add(new CommonItem("Form", "动态表单", item -> startActivity(new Intent(getActivity(),MultiFormActivity.class))));
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
                Broccoli broccoli = mPlaceholderManager.get(helper.itemView);
                if (broccoli == null){
                    broccoli = new Broccoli();
                    mPlaceholderManager.put(helper.itemView, broccoli);
                }
                broccoli.removeAllPlaceholders();
//                broccoli.addPlaceholders((ViewGroup) helper.itemView,R.id.tv_name,R.id.tv_content);
                broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                        .setView(helper.getView(R.id.tv_name))
                        .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#0DC787"),
                                Color.parseColor("#4EF08B"), dp2px(2), 1000, new LinearInterpolator()))
                        .build());
                broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                        .setView(helper.getView(R.id.tv_content))
                        .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                                Color.parseColor("#CCCCCC"), dp2px(2), 1000, new LinearInterpolator()))
                        .build());
                broccoli.show();
                Runnable task = mTaskManager.get(helper.itemView);
                if (task == null){
                    final Broccoli finalBroccoli = broccoli;
                    task = () -> {
                        //when you need to update data, you must to call the remove or the clear method.
                        finalBroccoli.removeAllPlaceholders();
                        helper.setText(R.id.tv_name,item.name);
                        helper.setText(R.id.tv_content,item.content);
                    };
                    mTaskManager.put(helper.itemView, task);
                }else{
                    helper.itemView.removeCallbacks(task);
                }
                helper.itemView.postDelayed(task, 2000);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (View view : mTaskManager.keySet()){
            view.removeCallbacks(mTaskManager.get(view));
        }

        //Prevent memory leaks when using BroccoliGradientDrawable
        //防止使用BroccoliGradientDrawable时内存泄露
        for (Broccoli broccoli : mPlaceholderManager.values()){
            broccoli.removeAllPlaceholders();
        }
        mPlaceholderManager.clear();
        mTaskManager.clear();
    }
}
