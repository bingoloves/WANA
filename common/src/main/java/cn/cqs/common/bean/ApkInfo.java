package cn.cqs.common.bean;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import cn.cqs.base.log.LogUtils;

public class ApkInfo {
    public static final int STATE_NOMAL = 0;
    public static final int STATE_INSTALLING = 1;
    public static final int STATE_INSTALLED = 2;

    @IntDef({STATE_NOMAL, STATE_INSTALLING, STATE_INSTALLED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * 当前的安装状态
     */
    @State
    private int installState = STATE_NOMAL;

    private Drawable icon;
    private String title;
    private String pluginName;
    private String versionName;
    private int versionCode;
    private String apkFilePath;
    private PackageInfo packageInfo;

    //public boolean installing = false;
    public void setInstallState(@State int state){
        this.installState = state;
    }

    public int getInstallState() {
        return installState;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getApkFilePath() {
        return apkFilePath;
    }

    public void setApkFilePath(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public ApkInfo(Context context, PackageInfo info,String pluginName, String path) {
        this.pluginName = pluginName;
        PackageManager pm = context.getPackageManager();
        Resources resources = null;
        try {
            resources = getResources(context, path);
        } catch (Exception e) {
        }
        try {
            if (resources != null) {
                icon = resources.getDrawable(info.applicationInfo.icon);
            }
        } catch (Exception e) {
            icon = pm.getDefaultActivityIcon();
        }
        try {
            if (resources != null) {
                title = resources.getString(info.applicationInfo.labelRes);
            }
        } catch (Exception e) {
            title = info.packageName;
        }
        versionName = info.versionName;
        versionCode = info.versionCode;
        apkFilePath = path;
        packageInfo = info;
    }

    public ApkInfo(PackageManager pm, PackageInfo info, String path) {
        try {
            icon = pm.getApplicationIcon(info.applicationInfo);
        } catch (Exception e) {
            icon = pm.getDefaultActivityIcon();
        }
        title = (String) pm.getApplicationLabel(info.applicationInfo);
        versionName = info.versionName;
        versionCode = info.versionCode;
        apkFilePath = path;
        packageInfo = info;
    }
    public static Resources getResources(Context context, String apkPath) throws Exception {
        String PATH_AssetManager = "android.content.res.AssetManager";
        Class assetMagCls = Class.forName(PATH_AssetManager);
        Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
        Object assetMag = assetMagCt.newInstance((Object[]) null);
        Class[] typeArgs = new Class[1];
        typeArgs[0] = String.class;
        Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",
                typeArgs);
        Object[] valueArgs = new Object[1];
        valueArgs[0] = apkPath;
        assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
        Resources res = context.getResources();
        typeArgs = new Class[3];
        typeArgs[0] = assetMag.getClass();
        typeArgs[1] = res.getDisplayMetrics().getClass();
        typeArgs[2] = res.getConfiguration().getClass();
        Constructor resCt = Resources.class.getConstructor(typeArgs);
        valueArgs = new Object[3];
        valueArgs[0] = assetMag;
        valueArgs[1] = res.getDisplayMetrics();
        valueArgs[2] = res.getConfiguration();
        res = (Resources) resCt.newInstance(valueArgs);
        return res;
    }

    public String getLaunchClassName() {
        return getLaunchClassName(getPackageInfo());
    }
    /**
     *  PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkInfo.getApkFilePath(), PackageManager.GET_ACTIVITIES);
     * 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等 并且可以直接调用
     */
    public String getLaunchClassName(PackageInfo info) {
        if(info != null){
            ActivityInfo[] activities = info.activities;
            if (activities != null){
                return activities[0].name;
            }
        }
        return null;
    }
}