package cn.cqs.common.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAGridDivider;
import cn.cqs.base.DensityUtils;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.R;
import cn.cqs.common.utils.PhotoUtils;
import cn.cqs.common.widget.MaxRecyclerView;

/**
 * Created by bingo on 2021/3/27.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 网格图片组件视图
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/27
 */
public class NineImageView extends FrameLayout {
    private GridLayoutManager layoutManager;
    private MaxRecyclerView recyclerView;
    private NineImageAdapter adapter;
    private NineViewCallback callback;
    /**
     * 是否可编辑
     *  编辑模式:有添加和删除
     *  非编辑查看模式:无数据时显示一个默认图
     */
    private boolean isEditable = true;
    /**
     * 最大添加数
     */
    private int max = 9;
    private int spanCount = 3;
    private Context context;
    /**
     * 默认的添加图片
     */
    private Drawable addImageDrawable;
    private String addImageText;
    public NineImageView(@NonNull Context context) {
        super(context);
        initView(context,null);
    }

    public NineImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public NineImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        this.context = context;
        if (attrs != null){
            initAttrs(context,attrs);
        }
        recyclerView = new MaxRecyclerView(context);
        recyclerView.setLayoutParams(new LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        addView(recyclerView);
        initRecyclerView();
    }

    /**
     * 获取自定义属性
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineImageView);
        if (typedArray != null) {
            spanCount = typedArray.getInt(R.styleable.NineImageView_spanCount, 3);
            max = typedArray.getInt(R.styleable.NineImageView_max, 9);
            isEditable = typedArray.getBoolean(R.styleable.NineImageView_editable, true);
            addImageDrawable = typedArray.getDrawable(R.styleable.NineImageView_addImage);
            addImageText = typedArray.getString(R.styleable.NineImageView_addImageText);
            typedArray.recycle();
        }
    }

    private void initRecyclerView() {
        layoutManager = new GridLayoutManager(context,spanCount);
        recyclerView.setLayoutManager(layoutManager);
        if (adapter == null){
            adapter = new NineImageAdapter(new ArrayList<>()){
                @Override
                protected void convertTypeAdd(BaseViewHolder helper, ImageBean item) {
                    super.convertTypeAdd(helper, item);
                    helper.addOnClickListener(R.id.root_add);
                    if (!TextUtils.isEmpty(addImageText)){
                        helper.setText(R.id.tv_add,addImageText);
                    }
                    if (addImageDrawable != null){
                        helper.setImageDrawable(R.id.iv_add,addImageDrawable);
                    }
                }

                @Override
                protected void convertTypeImage(BaseViewHolder helper, ImageBean item) {
                    super.convertTypeImage(helper, item);
                    ImageView photoIv = helper.getView(R.id.iv_image);
                    helper.setGone(R.id.iv_close, isEditable);
                    PhotoUtils.load(photoIv.getContext(),item.getPath(),photoIv);
                    helper.addOnClickListener(R.id.iv_image);
                    helper.addOnClickListener(R.id.iv_close);
                }

                @Override
                public int getItemViewType(int position) {
                    if (isEditable){
                        return (position == getData().size()) ? ImageBean.TYPE_ADD : ImageBean.TYPE_IMAGE;
                    }
                    return super.getItemViewType(position);
                }

                @Override
                public int getItemCount() {
                    if (isEditable){
                        int count = getData().size();
                        if (count >= max) {
                            count = max;
                        }
                        count = count + 1;
                        return count;
                    }
                    return super.getItemCount();
                }
            };
            recyclerView.addItemDecoration(BGAGridDivider.newInstanceWithSpaceDp(4));
            recyclerView.setAdapter(adapter);
        }
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            int i = view.getId();
            if (i == R.id.root_add) {
                if (callback != null) callback.addImage();
            } else if (i == R.id.iv_image) {
                if (callback != null) callback.onPreview(position, getImagePaths());
            } else if (i == R.id.iv_close) {
                if (callback != null) callback.deleteImage(position);
            } else {
                LogUtils.d("nothing");
            }
        });
    }
    /**
     * 添加单张图片
     * @param path
     */
    public void addImage(String path){
        ImageBean bean = new ImageBean();
        bean.setItemType(ImageBean.TYPE_IMAGE);
        bean.setPath(path);
        adapter.addData(bean);
    }

    /**
     * 添加多张图片
     * @param images
     */
    public void addImages(List<String> images){
        if (images != null){
            List<ImageBean> temp = new ArrayList<>();
            for (String image : images) {
                ImageBean bean = new ImageBean();
                bean.setItemType(ImageBean.TYPE_IMAGE);
                bean.setPath(image);
                temp.add(bean);
            }
            adapter.addData(temp);
        }
    }

    /**
     * 判断当前是否可添加数据
     * @return
     */
    private boolean isAddable(){
        return getImageBeans().size() < max;
    }

    /**
     * 移除数据
     * @param position
     */
    public void remove(int position){
        adapter.remove(position);
    }

    /**
     * 清空已有数据
     */
    public void clear(){
        List<ImageBean> images = getImageBeans();
       adapter.getData().removeAll(images);
       adapter.notifyDataSetChanged();
    }

    /**
     * 获取全部图片数据
     * @return
     */
    public List<String> getImagePaths(){
        List<ImageBean> data = adapter.getData();
        List<String> result = new ArrayList<>();
        for (ImageBean imageBean : data) {
            if (imageBean.getItemType() == ImageBean.TYPE_IMAGE){
                result.add(imageBean.getPath());
            }
        }
        return result;
    }
    /**
     * 获取全部图片数据
     * @return
     */
    public List<ImageBean> getImageBeans(){
        List<ImageBean> data = adapter.getData();
        List<ImageBean> result = new ArrayList<>();
        for (ImageBean imageBean : data) {
            if (imageBean.getItemType() == ImageBean.TYPE_IMAGE){
                result.add(imageBean);
            }
        }
        return result;
    }
    /**
     * 获取当前图片选择组件是否可编辑
     * @return
     */
    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    /**
     * 获取当前可添加数据
     * @return
     */
    public int getAddableNum(){
        return max - getImageBeans().size();
    }
    /**
     * 设置最大照片数
     * @param max
     */
    public void setMax(int max){
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    /**
     * 设置列数,默认3列
     * @param spanCount
     */
    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        layoutManager.setSpanCount(spanCount);
    }

    /**
     * 获取当前图片的比例 字符串
     * @return 如：1/4
     */
    @SuppressLint("DefaultLocale")
    public String getRatio(){
        return String.format("%d/%d", getImageBeans().size(), max);
    }

    public void setNineViewCallback(NineViewCallback callback) {
        this.callback = callback;
    }

    public class GridItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public GridItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            outRect.left = space;
            outRect.bottom = space;
            if (parent.getChildLayoutPosition(view) % spanCount == 0) {
                outRect.left = 0;
            }
        }
    }
}
