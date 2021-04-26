package cn.cqs.wana;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.Log;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import cn.cqs.base.ResourceUtils;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.adapter.QuickAdapter;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.bean.ApkInfo;
import cn.cqs.common.utils.PermissionUtils;
import cn.cqs.common.utils.RvItemDecoration;
import static com.morgoo.helper.compat.PackageManagerCompat.INSTALL_FAILED_NOT_SUPPORT_ABI;
import static com.morgoo.helper.compat.PackageManagerCompat.INSTALL_SUCCEEDED;

/**
 * Created by bingo on 2021/4/25.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 360手机助手 提供的插件化框架
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/25
 */

public class PluginActivity extends BaseActivity implements ServiceConnection {

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
        if (PluginManager.getInstance().isConnected()) {
            PermissionUtils.request(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, (allGranted, grantedList, deniedList) -> {
                if (allGranted){
                    startLoadInner();
                }
            });
        } else {
            PluginManager.getInstance().addServiceConnection(this);
        }
    }

    /**
     * 初始化插件目录
     */
    private void initPluginDir() {
        pluginDir = new File(getFilesDir(),"plugin");
        if (!pluginDir.exists()){
            pluginDir.mkdir();
        }
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }

    private void initAdapter(){
        adapter = new QuickAdapter<ApkInfo>(this,R.layout.list_item_plugin,new ArrayList<>()){
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ApkInfo item) {
                super.convert(helper, item);
                helper.setImageDrawable(R.id.iv_logo,item.getIcon());
                helper.setText(R.id.tv_app_name,item.getTitle());
                helper.setText(R.id.tv_version,String.format("%s(%s)", item.getVersionName(), item.getVersionCode()));
                helper.setText(R.id.tv_install,item.getInstallState() == ApkInfo.STATE_INSTALLED?"打开":"安装");
                helper.setGone(R.id.tv_uninstall,item.getInstallState() == ApkInfo.STATE_INSTALLED);
                helper.addOnClickListener(R.id.tv_install);
                helper.addOnClickListener(R.id.tv_uninstall);
            }
        };
        adapter.attachRecyclerView(recyclerView);
        //adapter.addCustomHeadView(this,12);
        recyclerView.addItemDecoration(new RvItemDecoration(this,RvItemDecoration.Vertical));
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            ApkInfo apkInfo = (ApkInfo) adapter.getData().get(position);
            switch (view.getId()){
                case R.id.tv_install:
                    if (apkInfo.getInstallState() == ApkInfo.STATE_INSTALLED){
                        openPlugin(apkInfo);
                    } else {
                        installApk(apkInfo,position);
                    }
                    break;
                case R.id.tv_uninstall:
                    toast("卸载");
                    uninstallApk(apkInfo,position);
                    break;
                default:
                    break;
            }
        });
    }
    private void initRefreshLayout(){
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(layout -> layout.finishRefresh(1000));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PermissionUtils.request(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, (allGranted, grantedList, deniedList) -> {
            if (allGranted){
                startLoadInner();
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogUtils.d("onServiceDisconnected : " + name.getClassName());
    }

    /**
     * 扫描内部插件Apk文件
     */
    private void startLoadInner() {
        new Thread("ApkScanner") {
            @Override
            public void run() {
//                File file = Environment.getExternalStorageDirectory();
//                File[] files = file.listFiles();
//                if (files != null) {
//                    for (File apk : files) {
//                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
//                            apks.add(apk);
//                        }
//                    }
//                }
//
//                file = new File(Environment.getExternalStorageDirectory(), "360Download");
//                if (file.exists() && file.isDirectory()) {
//                    File[] files1 = file.listFiles();
//                    if (files1 != null) {
//                        for (File apk : files1) {
//                            if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
//                                apks.add(apk);
//                            }
//                        }
//                    }
//                }
                List<File> apks = new ArrayList<File>(10);
                File[] files = filterApkFile();
                if (files != null){
                    if (files.length == 0){
                        boolean copyResult = ResourceUtils.copyFileFromAssets(getActivity(), "plugin", pluginDir.getPath());
                        if (copyResult) files = filterApkFile();
                    }
                    apks.addAll(Arrays.asList(files));
                }
                PackageManager pm = getActivity().getPackageManager();
                List<ApkInfo> list = new ArrayList<>();
                for (final File apk : apks) {
                    try {
                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                            final PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), 0);
                            if (info != null) {
                                ApkInfo apkInfo = new ApkInfo(getActivity(), info, apk.getPath());
                                if (checkInstalled(apkInfo)){
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
     * @return
     */
    private File[] filterApkFile(){
        return pluginDir.listFiles((dir, name) -> name.endsWith(".apk"));
    }
    private boolean checkInstalled(ApkInfo apkInfo){
        try {
            return PluginManager.getInstance().getPackageInfo(apkInfo.getPackageInfo().packageName, 0) != null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 打开插件
     * @param item
     */
    private void openPlugin(ApkInfo item){
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(item.getPackageInfo().packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            LogUtils.d("pm " + pm.toString() + " no find intent " + item.getPackageInfo().packageName);
        }
    }
    /**
     * 安装Apk
     * @param item
     */
    private void installApk(ApkInfo item,int position){
        if (item.getInstallState() == ApkInfo.STATE_INSTALLING) {
            return;
        }
        if (!PluginManager.getInstance().isConnected()) {
            toast("插件服务正在初始化，请稍后再试...");
        }
        try {
            if (PluginManager.getInstance().getPackageInfo(item.getPackageInfo().packageName, 0) != null) {
                item.setInstallState(ApkInfo.STATE_INSTALLED);
                runOnUiThread(() -> adapter.notifyItemChanged(position));
                toast("已经安装了，不能再安装");
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        item.setInstallState(ApkInfo.STATE_INSTALLING);
                        runOnUiThread(() -> adapter.notifyItemChanged(position));
                        try {
                            final int result = PluginManager.getInstance().installPackage(item.getApkFilePath(), 0);
                            runOnUiThread(() -> {
                                switch (result) {
                                    case PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION:
                                        item.setInstallState(ApkInfo.STATE_NOMAL);
                                        toast("安装失败，文件请求的权限太多");
                                        break;
                                    case INSTALL_FAILED_NOT_SUPPORT_ABI:
                                        item.setInstallState(ApkInfo.STATE_NOMAL);
                                        toast("宿主不支持插件的abi环境，可能宿主运行时为64位，但插件只支持32位");
                                        break;
                                    case INSTALL_SUCCEEDED:
                                        item.setInstallState(ApkInfo.STATE_INSTALLED);
                                        toast("安装完成");
                                        break;
                                }
                                adapter.notifyItemChanged(position);
                            });
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 卸载Apk
     * @param item
     */
    private void uninstallApk(final ApkInfo item,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("警告，你确定要卸载么？");
        builder.setMessage("警告，你确定要卸载" + item.getTitle() + "么？");
        builder.setNegativeButton("卸载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    PluginManager.getInstance().deletePackage(item.getPackageInfo().packageName, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                item.setInstallState(ApkInfo.STATE_NOMAL);
                adapter.notifyItemChanged(position);
//                new File(item.getApkFilePath()).delete();
//                adapter.remove(position);
                toast("卸载成功");
            }
        });
        builder.setNeutralButton("取消", null);
        builder.show();
    }
}
