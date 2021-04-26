package cn.cqs.common.utils;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

/**
 * Created by bingo on 2021/3/4.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: mmkv数据缓存工具
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/4
 */

public class Cache {
    MMKV mmkv;
    private Cache() {
        MMKV.initialize(AppUtils.getInstance().getApplication());
        mmkv = MMKV.defaultMMKV();
    }
    public static Cache get() {
        return SingletonHolder.sIntance;
    }
    private static class SingletonHolder{
        private static final Cache sIntance = new Cache();
    }

    public MMKV getCache() {
        return mmkv;
    }

    /**
     * 新增保存对象
     * @param key
     * @param object
     */
    public void put(String key,Object object){
        mmkv.encode(key,object == null ? "" : new Gson().toJson(object));
    }

    /**
     * 获取对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> clazz){
        String value = mmkv.decodeString(key, "");
        return new Gson().fromJson(value,clazz);
    }
}
