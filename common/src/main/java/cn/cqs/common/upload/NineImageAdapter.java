package cn.cqs.common.upload;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

import cn.cqs.common.R;

/**
 * Created by bingo on 2021/4/16.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/16
 */
public class NineImageAdapter extends BaseMultiItemQuickAdapter<ImageBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NineImageAdapter(List<ImageBean> data) {
        super(data);
        addItemType(ImageBean.TYPE_ADD, R.layout.common_add_view);
        addItemType(ImageBean.TYPE_IMAGE, R.layout.common_grid_image_view);
    }
    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType){
            case ImageBean.TYPE_ADD:
                convertTypeAdd(helper,item);
                break;
            case ImageBean.TYPE_IMAGE:
                convertTypeImage(helper,item);
                break;
            default:
                break;
        }
    }

    /**
     * 解析图片类型
     * @param helper
     * @param item
     */
    protected void convertTypeImage(BaseViewHolder helper, ImageBean item) {

    }

    /**
     * 添加类型
     * @param helper
     * @param item
     */
    protected void convertTypeAdd(BaseViewHolder helper, ImageBean item) {

    }
}
