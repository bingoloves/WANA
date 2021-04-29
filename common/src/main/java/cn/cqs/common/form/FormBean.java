package cn.cqs.common.form;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.cqs.common.enums.FormEnum;

/**
 * Created by bingo on 2021/4/29.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 动态表单类
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/29
 */

public class FormBean implements MultiItemEntity{
    /**
     * 是否必填
     */
    private boolean mast = false;
    /**
     * 表单类型
     */
    private FormEnum type = FormEnum.Input;
    /**
     * 表单Id
     */
    private int id;
    /**
     * 名称
     */
    private String name;
    /**
     * 数值
     */
    private String value;
    /**
     * 最后的包装字段名
     */
    private String fieldName;
    /**
     * 表单初始化接口
     */
    private IFormConfigure formConfigure;
    /**
     * 选择可选值
     */
    private List<FormValue> values;

    /**
     * 标题类
     * @param type
     * @param name
     */
    public FormBean(FormEnum type, String name) {
        this.type = type;
        this.name = name;
    }

    public FormBean(FormEnum type, boolean mast, String name, String fieldName, String value, List<FormValue> values, IFormConfigure formConfigure) {
        this.mast = mast;
        this.type = type;
        this.name = name;
        this.value = value;
        this.fieldName = fieldName;
        this.formConfigure = formConfigure;
        this.values = values;
    }

    public boolean isMast() {
        return mast;
    }

    public void setMast(boolean mast) {
        this.mast = mast;
    }

    public FormEnum getType() {
        return type;
    }

    public void setType(FormEnum type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public IFormConfigure getFormConfigure() {
        return formConfigure;
    }

    public void setFormConfigure(IFormConfigure formConfigure) {
        this.formConfigure = formConfigure;
    }

    public List<FormValue> getValues() {
        return values;
    }

    public void setValues(List<FormValue> values) {
        this.values = values;
    }

    @Override
    public int getItemType() {
        return type.getValue();
    }

}
