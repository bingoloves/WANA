package cn.cqs.common.base;

import android.app.Application;
import android.util.TypedValue;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import cn.cqs.base.AppExecutors;
import cn.cqs.base.ResourceUtils;
import cn.cqs.base.TypefaceUtil;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.R;
import cn.cqs.common.utils.AppUtils;
import cn.cqs.common.utils.DynamicTimeFormat;
import cn.cqs.toast.ToastUtils;
import cn.cqs.toast.style.ToastAliPayStyle;

/**
 * Created by bingo on 2021/4/23.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/23
 */

public abstract class BaseApplication extends Application{
    public abstract boolean isDebug();
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            return new ClassicsHeader(context)
                    .setArrowResource(R.drawable.ic_arrow_down)
                    .setTextSizeTitle(TypedValue.COMPLEX_UNIT_SP,12)
                    .setTextSizeTime(TypedValue.COMPLEX_UNIT_SP,10)
                    .setTimeFormat(new DynamicTimeFormat("更新于 %s"))
//                    .setEnableLastTime(false)
                    ;
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.replaceSystemDefaultFont(this,"pingfang.ttf");
        AppUtils.getInstance().init(this);
        ToastUtils.init(this, new ToastAliPayStyle(this));
        LogUtils.init(this,isDebug());
//        initARouter();
        LiveEventBus.config().
                supportBroadcast(this).
                lifecycleObserverAlwaysActive(true).
                autoClear(false);
        openBlackTask();
    }

//    private void initARouter() {
//        if (isDebug()) {
//            ARouter.openLog();     // 打印日志
//            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
//        ARouter.init(this);
//    }

    private void openBlackTask() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            String cityJson = ResourceUtils.readAssets2String(this, "city.json");
            //cityBeanList = new Gson().fromJson(cityJson, new TypeToken<List<CityBean>>(){}.getType());
            String agreementJson = ResourceUtils.readAssets2String(this, "agreement.json");
            //agreementList = new Gson().fromJson(agreementJson, new TypeToken<List<Agreement>>(){}.getType());
        });
    }
}
