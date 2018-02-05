package com.xwjr.utilcode.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 主要通过收听ANR的广播,来检测是否发生ANR的现象,但是无法阻止ANR
 * <p>
 * <p>
 * 在onReceive()里面做判断
 * if (intent.getAction().equals(ACTION_ANR)) {
 * // do you want to do
 * }
 *
 * @author aaa
 */
public class ANRCacheHelper {

    private static MyReceiver myReceiver;

    public static void registerANRReceiver(Context context) {
        try {
            myReceiver = new MyReceiver();
            context.registerReceiver(myReceiver, new IntentFilter(ACTION_ANR));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void unregisterANRReceiver(Context context) {
        try {
            if (myReceiver == null) {
                return;
            }
            context.unregisterReceiver(myReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String ACTION_ANR = "android.intent.action.ANR";

    private static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(ACTION_ANR)) {
                    // to do
                    LogUtils.i("发生了ANR异常");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}