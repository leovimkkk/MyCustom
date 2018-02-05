package com.xwjr.utilcode.customview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xwjr.utilcode.R;
import com.xwjr.utilcode.utils.ImageUtils;
import com.xwjr.utilcode.utils.SizeUtils;


/**
 * 带删除按钮的文本编辑框
 * init(Context context)可设置删除按钮的样式
 */
public class MyEditTextHaveDeleteButton extends AppCompatEditText {
    private final static String TAG = "EditTextWithDel";
    private Drawable imgInable;
    private Drawable imgAble;
    private boolean hideIcon;

    public MyEditTextHaveDeleteButton(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public MyEditTextHaveDeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public MyEditTextHaveDeleteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(context);
    }


    private void init(Context context) {
        //imgInable = mContext.getResources().getDrawable(R.drawable.delete_gray);
        Drawable deleteDrawable = context.getResources().getDrawable(R.mipmap.edittext_delete_button);
        imgAble = zoomDrawable(deleteDrawable, SizeUtils.dp2px(50), SizeUtils.dp2px(50));
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
        setOnFocusChangeListener(onFocusChangeListener);

    }


    public void setDrawable() {
        if (length() < 1) {
            //setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {

            if (hideIcon) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
            }

        }
    }

    public void clearDrawable() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();

            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public boolean isShowIcon() {
        return hideIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.hideIcon = showIcon;
    }

    OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!v.hasFocus()) {
                clearDrawable();
            } else {
                setDrawable();
            }
        }
    };

    static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = ImageUtils.drawable2Bitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象
        float scaleWidth = ((float) w / width);   // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
        return new BitmapDrawable(newbmp);       // 把 bitmap 转换成 drawable 并返回
    }
}
