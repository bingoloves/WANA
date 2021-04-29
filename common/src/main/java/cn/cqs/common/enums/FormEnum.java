package cn.cqs.common.enums;

/**
 * Created by bingo on 2021/4/29.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 表单类型
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/29
 */

public enum  FormEnum {
    /**
     * 单独标题
     */
    Title(0),
    /**
     * 单选
     */
    Radio(1),
    /**
     * 多选
     */
    Checkbox(2),
    /**
     * 输入框
     */
    Input(3),
    /**
     * 日期
     */
    Date(4),
    /**
     * 时间
     */
    Time(5),
    /**
     * 图片类型
     */
    Image(6),
    /**
     * 选择类型
     */
    Select(7);
    private int value;
    FormEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
