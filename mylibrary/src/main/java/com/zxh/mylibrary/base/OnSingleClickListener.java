package com.zxh.mylibrary.base;

import android.view.View;

import com.xwjr.utilcode.utils.LogUtils;

/**
 * Created by Administrator on 2017/4/1.
 */

public abstract class OnSingleClickListener implements View.OnClickListener {
    private long delayTime = 500;
    private static long lastTime;

    public abstract void singleClick(View v);


    @Override
    public void onClick(View v) {
        if (!((System.currentTimeMillis() - lastTime) < delayTime)) {
            try {
                singleClick(v);
                lastTime = System.currentTimeMillis();
            }catch (Exception e){
                LogUtils.i("OnSingleClickListener-->单击发生异常");
            }
        }else{

        }
    }
}
