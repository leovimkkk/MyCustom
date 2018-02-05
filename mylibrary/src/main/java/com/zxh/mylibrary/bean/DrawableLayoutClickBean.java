package com.zxh.mylibrary.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by zxh on 2018/2/5.
 * 单条点击的Bean
 */

public class DrawableLayoutClickBean {
    private String Key = "";
    private String value = "";
    private Drawable drawableRight;
    private Drawable drawableLeft;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Drawable getDrawableRight() {
        return drawableRight;
    }

    public void setDrawableRight(Drawable drawableRight) {
        this.drawableRight = drawableRight;
    }

    public Drawable getDrawableLeft() {
        return drawableLeft;
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        this.drawableLeft = drawableLeft;
    }
}
