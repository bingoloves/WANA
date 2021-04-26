package cn.cqs.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

import cn.cqs.common.R;
import cn.cqs.common.utils.SpacesItemDecoration;
import cn.cqs.common.widget.SpaceHeaderView;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    //多状态视图
    private View emptyView;
    private View errorView;
    private View loadingView;
    private Context context;
    private View.OnClickListener emptyClickListener;
    private View.OnClickListener errorClickListener;

    public QuickAdapter(Context context,int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        this.context = context;
        initEmptyView();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, T item) {

    }

    /**
     * 获取当前Holder位置的数据索引
     * @param helper
     * @return
     */
    public int getCurrentPosition(@NonNull BaseViewHolder helper){
        return helper.getLayoutPosition() - getHeaderLayoutCount();
    }
    public void attachRecyclerView(RecyclerView recyclerView){
        if (recyclerView == null) throw new IllegalArgumentException("recyclerView == null");
        attachRecyclerView(recyclerView,new LinearLayoutManager(recyclerView.getContext()));
    }
    public void attachRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager){
        if (recyclerView == null) throw new IllegalArgumentException("recyclerView == null");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);
    }

    /**
     * 添加头部和底部间距
     * @param height dp
     */
    public void addHeadFootSpace(int height){
        addHeadSpace(height);
        addFootSpace(height);
    }
    /**
     * 添加头部间距
     * @param height dp
     */
    public void addHeadSpace(int height){
        addHeaderView(getSpaceView(height));
    }
    /**
     * 添加底部间距
     * @param height
     */
    public void addFootSpace(int height){
        addFooterView(getSpaceView(height));
    }

    public View getSpaceView(int height){
        return new SpaceHeaderView(context,height);
    }
    public View getSpaceView(int height,int color){
        return new SpaceHeaderView(context,height,color);
    }

    /**
     * 初始化空视图、错误视图、加载视图
     */
    public void initEmptyView(){
        loadingView = LayoutInflater.from(context).inflate(R.layout.common_loading_view, null);
        emptyView = LayoutInflater.from(context).inflate(R.layout.common_empty_view, null);
        errorView = LayoutInflater.from(context).inflate(R.layout.common_error_view, null);
        //设置空视图,首次加载页面时，不显示
        setEmptyView(emptyView);
        emptyView.setVisibility(View.GONE);
//        View footView = LayoutInflater.from(context).inflate(R.layout.common_foot_view, (ViewGroup) recyclerView.getParent(), false);
//        addFooterView(footView,0);
//        //设置空视图和header/footer可以共存，footer共存必须recyclerView 高度wrap_content
//        setHeaderFooterEmpty(true,true);
    }

    /**
     * 显示加载视图
     */
    public void showLoading(){
        setEmptyView(loadingView);
    }

    /**
     * 显示空视图
     */
    public void showEmptyView(){
        emptyView.setVisibility(View.VISIBLE);
        setEmptyView(emptyView);
    }

    /**
     * 显示错误视图
     */
    public void showErrorView(){
        setEmptyView(errorView);
    }

    public void setEmptyClickListener(View.OnClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
        emptyView.setOnClickListener(emptyClickListener);
    }

    public void setErrorClickListener(View.OnClickListener errorClickListener) {
        this.errorClickListener = errorClickListener;
        errorView.setOnClickListener(errorClickListener);
    }
}
