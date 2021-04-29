package cn.cqs.common.form;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import cn.cqs.common.R;

/**
 * Created by bingo on 2021/4/16.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 单选和多选适配器
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/16
 */
public class SelectAdapter extends BaseMultiItemQuickAdapter<FormValue, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SelectAdapter(List<FormValue> data) {
        super(data);
        addItemType(FormValue.RAIDO, R.layout.common_item_radio);
        addItemType(FormValue.CHECKBOX, R.layout.common_item_checkbox);
    }
    @Override
    protected void convert(BaseViewHolder helper, FormValue item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType){
            case FormValue.RAIDO:
                convertTypeRadio(helper,item);
                break;
            case FormValue.CHECKBOX:
                convertTypeCheckbox(helper,item);
                break;
            default:
                break;
        }
    }

    /**
     * 单选类型
     * @param helper
     * @param item
     */
    protected void convertTypeRadio(BaseViewHolder helper, FormValue item) {

    }

    /**
     * 多选类型
     * @param helper
     * @param item
     */
    protected void convertTypeCheckbox(BaseViewHolder helper, FormValue item) {

    }
}
