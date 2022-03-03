package com.base.wanandroid.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.base.wanandroid.BuildConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AppUtils {
    private static String sCurrentProcess = null; // 当前进程名
    private static String sChannel = null; // 渠道号
    private static String sVersionName = null;
    private static int sVersionCode = 0;
    private static final String CAMPAIGN_KEY = "ACTIVE_CAMPAIGN";
    private static final String CHANNEL_KEY = "UMENG_CHANNEL";
    private static int sTargetSdkVersion = -1;
    private static String sActiveCampaign = null;


    public static int getTargetSdkVersion(Context context) {
        if (sTargetSdkVersion > 0) {
            return sTargetSdkVersion;
        }
        int v = -1;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            v = applicationInfo.targetSdkVersion;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (v > 0) {
            sTargetSdkVersion = v;
        }
        return sTargetSdkVersion;
    }


    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName(Context context) {
        if (!TextUtils.isEmpty(sVersionName)) {
            return sVersionName;
        }
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            sVersionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sVersionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        if (sVersionCode != 0) {
            return sVersionCode;
        }
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            sVersionCode = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sVersionCode;
    }


    public static boolean inMainProcess(Context ctx) {
        final boolean result = ctx.getPackageName().equals(getProcessName(Process.myPid()));
        if (BuildConfig.LOG_ENABLE) {
            Log.d("PROCESS", String.format("%s : %s : %b", ctx.getPackageName(), getProcessName(Process.myPid()), result));
        }
        return result;
    }


    /**
     * 获取进程号对应的进程名，推荐使用的方式
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        if (TextUtils.isEmpty(sCurrentProcess)) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
                String processName = reader.readLine();
                if (!TextUtils.isEmpty(processName)) {
                    sCurrentProcess = processName.trim();
                }
                return sCurrentProcess;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return sCurrentProcess;
    }

    public static boolean toBrowser(Context context, String uriString) {
        boolean result = false;
        if (context == null || TextUtils.isEmpty(uriString)) {
            return result;
        }
        Uri browserUri = Uri.parse(uriString);
        if (null != browserUri) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(browserIntent);
                result = true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
