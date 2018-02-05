package com.xwjr.utilcode.customview.addressPicker;

import android.content.Context;

import com.xwjr.utilcode.customview.datePicker.ConvertUtils;
import com.xwjr.utilcode.utils.LogUtils;

/**
 * 操作安装包中的“assets”目录下的文件
 *
 */
public class AssetsUtils {

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static String readText(Context context, String assetPath) {
        LogUtils.i(AssetsUtils.class.getSimpleName(),"read assets file as text: " + assetPath);
        try {
            return ConvertUtils.toString(context.getAssets().open(assetPath));
        } catch (Exception e) {
            LogUtils.i(AssetsUtils.class.getSimpleName(),e.toString());
            return "";
        }
    }

}
