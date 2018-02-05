package com.xwjr.utilcode.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ViewUtil {
    /**
     * 动态设置listView的高度 count 总条目
     */
    public static void setListViewHeight(ListView listView) {
        int totalHeight = 0;
        for (int i = 0; i < listView.getCount(); i++) {
            View listItem = listView.getAdapter().getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listView.getCount());
        listView.setLayoutParams(params);
    }

    public static int getViewHeight(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        return view.getMeasuredHeight();
    }

    public static int getViewWidth(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        return view.getMeasuredWidth();
    }

    public static void setViewRelativeWidthHeight(View view, int width, int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (width != 0)
            layoutParams.width = width;
        if (height != 0)
            layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setViewLinearWidthHeight(View view, int width, int height) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (width != 0)
            layoutParams.width = width;
        if (height != 0)
            layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    public static int getWindowWidth() {
        WindowManager wm = (WindowManager) Utils.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }
    public static int getWindowHeight() {
        WindowManager wm = (WindowManager) Utils.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }

    public static double getWeight(double a) {
        return a / 750.0;
    }
}
