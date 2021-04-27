package cn.cqs.common.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.cqs.base.ActivityStackManager;
import cn.cqs.base.AppExecutors;
import cn.cqs.base.ResourceUtils;
import cn.cqs.base.TypefaceUtil;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.R;
import cn.cqs.common.crash.Cockroach;
import cn.cqs.common.crash.ExceptionHandler;
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
        initCrash();
        LiveEventBus.config().
                supportBroadcast(this).
                lifecycleObserverAlwaysActive(true).
                autoClear(false);
        openBlackTask();
    }

    /**
     * 子线程操作耗时任务
     */
    private void openBlackTask() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            String cityJson = ResourceUtils.readAssets2String(this, "city.json");
            //cityBeanList = new Gson().fromJson(cityJson, new TypeToken<List<CityBean>>(){}.getType());
            String agreementJson = ResourceUtils.readAssets2String(this, "agreement.json");
            //agreementList = new Gson().fromJson(agreementJson, new TypeToken<List<Agreement>>(){}.getType());
        });
    }

    /**
     * 全局异常捕获，避免App崩溃
     */
    private void initCrash() {
        Cockroach.install(this,new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                LogUtils.d("--->onUncaughtExceptionHappened:" + thread + "<---" + getExcptionInfo(throwable));
                if (isDebug()){
                    new Handler(Looper.getMainLooper()).post(() -> ToastUtils.show("捕获到导致崩溃的异常,请相关开发人员查看"));
                }
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                //打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
                LogUtils.d("异常捕获：" + getExcptionInfo(throwable));
            }

            @Override
            protected void onEnterSafeMode() {
                LogUtils.d("已经进入安全模式");
            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                Thread thread = Looper.getMainLooper().getThread();
                LogUtils.d("--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
                ActivityStackManager.getStackManager().AppExit();
            }
        });
    }
    /**
     * 获取异常信息
     * @param throwable
     * @return
     */
    private String getExcptionInfo(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
