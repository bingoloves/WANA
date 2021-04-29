package cn.cqs.common.form;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by bingo on 2021/4/29.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 表单子项
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/29
 */

public class FormValue implements MultiItemEntity{
    public static final int RAIDO = 0;
    public static final int CHECKBOX = 1;
    private String name;
    private String value;
    private boolean selected = false;
    private int type = CHECKBOX;
    public FormValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public FormValue(String name, String value, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
