package com.xwjr.utilcode.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * isActivityExists : 判断是否存在Activity
 * launchActivity   : 打开Activity
 * </pre>
 */
public class ActivityUtils {

    public static List<Activity> activityList = new ArrayList<>();

    //添加一个activity
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //一处一个Activity
    public static void removeActivity(Activity currentActivity) {
        for (Activity activity : activityList) {
            if (activity == currentActivity) {
                activityList.remove(activity);
            }
        }
    }

    //退出Activity
    public static void exitActivity() {
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
    }

    public static boolean isHaveMainActivity(){
        for (Activity activity : activityList) {
            if (activity.getClass().getSimpleName().equals("MainActivity"))
                return true;
        }
        return false;
    }

    //返回MainActivity
    public static void backToMainActivity() {
        for (Activity activity : activityList) {
            if (!activity.getClass().getSimpleName().equals("MainActivity"))
                activity.finish();
        }
    }

    //获得已存在Activity对象
    public static Activity getActivity(String activityName) {
        for (Activity activity : activityList) {
            if (activity.getClass().getSimpleName().equals(activityName)) {
                return activity;
            }
        }
        return null;
    }


    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断是否存在Activity
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isActivityExists(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(Utils.getContext().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(Utils.getContext().getPackageManager()) == null ||
                Utils.getContext().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     */
    public static void launchActivity(String packageName, String className) {
        launchActivity(packageName, className, null);
    }

    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    public static void launchActivity(String packageName, String className, Bundle bundle) {
        Utils.getContext().startActivity(IntentUtils.getComponentIntent(packageName, className, bundle));
    }

    /**
     * 获取launcher activity
     *
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = Utils.getContext().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return "no " + packageName;
    }


    /**
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                activities = (HashMap) activitiesField.get(activityThread);
            } else {
                activities = (ArrayMap) activitiesField.get(activityThread);
            }
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
