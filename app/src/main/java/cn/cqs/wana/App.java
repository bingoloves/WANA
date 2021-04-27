package cn.cqs.wana;

import android.content.Context;

import com.didi.virtualapk.PluginManager;
import cn.cqs.common.base.BaseApplication;

/**
 * Created by bingo on 2021/4/23.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/23
 */

public class App extends BaseApplication {
    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
//        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }
}
