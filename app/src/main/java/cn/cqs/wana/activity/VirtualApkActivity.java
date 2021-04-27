package cn.cqs.wana.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.didi.virtualapk.PluginManager;
import com.didi.virtualapk.internal.LoadedPlugin;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.cqs.base.DensityUtils;
import cn.cqs.base.ResourceUtils;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.adapter.QuickAdapter;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.bean.ApkInfo;
import cn.cqs.common.bean.EventBean;
import cn.cqs.common.utils.LiveEventBusUtils;
import cn.cqs.common.utils.PermissionUtils;
import cn.cqs.common.utils.SpacesItemDecoration;
import cn.cqs.wana.R;
/**
 * Created by bingo on 2021/4/25.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: didi VirtualApk 提供的插件化框架
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/25
 */

public class VirtualApkActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TextView titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    QuickAdapter adapter;

    private File pluginDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        initPluginDir();
        initAdapter();
        initRefreshLayout();
        PermissionUtils.request(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, (allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                startLoadInner();
            }
        });
        LiveEventBusUtils.register(this, bean -> {
            switch (bean.getType()){
                case 0:
                case 1:
                    LogUtils.e((String) bean.getBean());
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 初始化插件目录
     */
    private void initPluginDir() {
        pluginDir = new File(getFilesDir(), "plugin");
        if (!pluginDir.exists()) {
            pluginDir.mkdir();
        }
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }

    private void initAdapter() {
        adapter = new QuickAdapter<ApkInfo>(this, R.layout.list_item_plugin, new ArrayList<>()) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ApkInfo item) {
                super.convert(helper, item);
                helper.setImageDrawable(R.id.iv_logo, item.getIcon());
                helper.setText(R.id.tv_app_name, item.getTitle());
                helper.setText(R.id.tv_version, String.format("%s(%s)", item.getVersionName(), item.getVersionCode()));
                helper.setText(R.id.tv_install, item.getInstallState() == ApkInfo.STATE_INSTALLED?"打开":"加载插件");
                helper.setGone(R.id.tv_uninstall, false);
                helper.addOnClickListener(R.id.tv_install);
                helper.addOnClickListener(R.id.tv_uninstall);
            }
        };
        adapter.attachRecyclerView(recyclerView);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL);
        itemDecoration.setParam(R.color.color_F6F8FC, DensityUtils.dp2px(this, 12));
        recyclerView.addItemDecoration(itemDecoration);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            ApkInfo apkInfo = (ApkInfo) adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_install:
                    if (apkInfo.getInstallState() == ApkInfo.STATE_INSTALLED){
                        String launchClassName = apkInfo.getLaunchClassName();
                        LogUtils.e(launchClassName);
                        if (!TextUtils.isEmpty(launchClassName)){
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName(apkInfo.getPackageInfo().packageName, launchClassName));
                            startActivity(intent);
                        }
                    } else {
                        try {
                            PluginManager.getInstance(getActivity()).loadPlugin(new File(apkInfo.getApkFilePath()));
                            apkInfo.setInstallState(ApkInfo.STATE_INSTALLED);
                            adapter.notifyItemChanged(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.tv_uninstall:
                    break;
                default:
                    break;
            }
        });
    }

    private void initRefreshLayout() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(layout -> layout.finishRefresh(1000));
    }

    /**
     * 扫描内部插件Apk文件
     */
    private void startLoadInner() {
        new Thread("ApkScanner") {
            @Override
            public void run() {
                List<File> apks = new ArrayList<File>(10);
                File[] files = filterApkFile();
                for (File file : files) {
                    file.delete();
                }
                boolean copyResult = ResourceUtils.copyFileFromAssets(getActivity(), "plugin", pluginDir.getPath());
                if (copyResult) files = filterApkFile();
                apks.addAll(Arrays.asList(files));
                PackageManager pm = getActivity().getPackageManager();
                List<ApkInfo> list = new ArrayList<>();
                for (final File apk : apks) {
                    try {
                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                            final PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), PackageManager.GET_ACTIVITIES);
                            if (info != null) {
                                ApkInfo apkInfo = new ApkInfo(getActivity(), info, apk.getName(), apk.getPath());
                                if (PluginManager.getInstance(getActivity()).getLoadedPlugin(apkInfo.getPackageInfo().packageName) != null) {
                                    apkInfo.setInstallState(ApkInfo.STATE_INSTALLED);
                                }
                                list.add(apkInfo);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(() -> adapter.setNewData(list));
            }
        }.start();
    }

    /**
     * 获取插件目录下的文件
     *
     * @return
     */
    private File[] filterApkFile() {
        return pluginDir.listFiles((dir, name) -> name.endsWith(".apk"));
    }
}