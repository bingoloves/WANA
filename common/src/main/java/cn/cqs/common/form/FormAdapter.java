package cn.cqs.common.form;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import cn.cqs.common.R;
import cn.cqs.common.enums.FormEnum;
import static cn.cqs.common.enums.FormEnum.Checkbox;
import static cn.cqs.common.enums.FormEnum.Radio;
import static cn.cqs.common.enums.FormEnum.Title;
import static cn.cqs.common.enums.FormEnum.Input;
import static cn.cqs.common.enums.FormEnum.Date;
import static cn.cqs.common.enums.FormEnum.Time;
import static cn.cqs.common.enums.FormEnum.Image;
import static cn.cqs.common.enums.FormEnum.Select;

/**
 * Created by bingo on 2021/4/16.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 动态表单适配器
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/16
 */
public class FormAdapter extends BaseMultiItemQuickAdapter<FormBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FormAdapter(List<FormBean> data) {
        super(data);
        addItemType(Title.getValue(), R.layout.common_form_title);
        addItemType(Radio.getValue(), R.layout.common_form_radio);
        addItemType(Checkbox.getValue(), R.layout.common_form_checkbox);
        addItemType(Input.getValue(), R.layout.common_form_input);
//        addItemType(Date.getValue(), R.layout.common_form_checkbox);
//        addItemType(Time.getValue(), R.layout.common_form_checkbox);
        addItemType(Image.getValue(), R.layout.common_form_image);
        addItemType(Select.getValue(), R.layout.common_form_select);
    }
    @Override
    protected void convert(BaseViewHolder helper, FormBean item) {
        int itemViewType = helper.getItemViewType();
        FormEnum formEnum = getFormEnum(itemViewType);
        IFormConfigure formConfigure = item.getFormConfigure();
        if (formConfigure != null){
            formConfigure.initView(helper.itemView);
        }
        if (formEnum != null){
            switch (formEnum){
                case Title:
                    convertTypeTitle(helper,item);
                    break;
                case Radio:
                    convertTypeRadio(helper,item);
                    break;
                case Checkbox:
                    convertTypeCheckbox(helper,item);
                    break;
                case Input:
                    convertTypeInput(helper,item);
                    break;
                case Date:
                    convertTypeDate(helper,item);
                    break;
                case Time:
                    convertTypeTime(helper,item);
                    break;
                case Image:
                    convertTypeImage(helper,item);
                    break;
                case Select:
                    convertTypeSelect(helper,item);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 根据类型获取对应枚举实例
     *
     * @param type
     * @return
     */
    public static FormEnum getFormEnum(int type) {
        for (FormEnum result : FormEnum.values()) {
            if (result.getValue() == type) {
                return result;
            }
        }
        return null;
    }

    /**
     * 对应类型的解析
     */
    protected void convertTypeTitle(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeRadio(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeCheckbox(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeInput(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeDate(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeTime(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeImage(BaseViewHolder helper, FormBean item) {}
    protected void convertTypeSelect(BaseViewHolder helper, FormBean item) {}
}
