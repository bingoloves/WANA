package cn.cqs.common.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.jeremyliao.liveeventbus.LiveEventBus;

import cn.cqs.common.bean.EventBean;

/**
 * Created by bingo on 2021/4/27.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/27
 */
public class LiveEventBusUtils {
    private static final String DEFAULT_KEY = "default_key";
    public static void post(EventBean bean){
        LiveEventBus.get(DEFAULT_KEY).post(bean);
    }
    public static void register(@NonNull LifecycleOwner owner,@NonNull Observer<EventBean> observer){
        LiveEventBus.get(DEFAULT_KEY,EventBean.class).observe(owner, observer);
    }
}
