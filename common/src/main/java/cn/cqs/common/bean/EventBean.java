package cn.cqs.common.bean;

/**
 * Created by bingo on 2021/4/27.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 事件对象
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/27
 */

public class EventBean<T> {
    private int type;
    private T t;

    public EventBean(int type, T t) {
        this.type = type;
        this.t = t;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getBean() {
        return t;
    }

    public void setBean(T t) {
        this.t = t;
    }
}
