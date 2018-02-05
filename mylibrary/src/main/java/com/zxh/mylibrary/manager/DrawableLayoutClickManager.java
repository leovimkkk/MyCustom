package com.zxh.mylibrary.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.zxh.mylibrary.bean.DrawableLayoutClickBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxh on 2018/2/5.
 * 单条点击的数据管理
 */

public class DrawableLayoutClickManager {
    private List<DrawableLayoutClickBean> dataList = new ArrayList<>();
    private Context context;

    public DrawableLayoutClickManager(List<DrawableLayoutClickBean> dataList){
        init(dataList);
    }

    private void init(List<DrawableLayoutClickBean> dataList) {
        this.dataList = dataList;
    }

    public void add(String key, String value) {
        add(key, value, null);
    }

    public void add(String key, String value, Drawable drawableLeft) {
        add(key, value, drawableLeft, null);
    }

    public void add(String key, String value, Drawable drawableLeft, Drawable drawableRight) {
        DrawableLayoutClickBean data = new DrawableLayoutClickBean();
        data.setKey(key);
        data.setValue(value);
        data.setDrawableLeft(drawableLeft);
        data.setDrawableRight(drawableRight);
        dataList.add(data);
    }

    public void remove(int i){
        dataList.remove(i);
    }

    public void removeByKey(String key){
        for(DrawableLayoutClickBean item : dataList){
            if(key.equals(item.getKey())){
                dataList.remove(item);
            }
        }
    }

    public void removeByValue(String value){
        for(DrawableLayoutClickBean item : dataList){
            if(value.equals(item.getValue())){
                dataList.remove(item);
            }
        }
    }


}
