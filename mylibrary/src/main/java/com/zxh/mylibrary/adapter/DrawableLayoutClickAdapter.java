package com.zxh.mylibrary.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwjr.utilcode.utils.LogUtils;
import com.xwjr.utilcode.utils.Utils;
import com.zxh.mylibrary.R;
import com.zxh.mylibrary.base.CommonAdapter;
import com.zxh.mylibrary.base.ViewHolder;
import com.zxh.mylibrary.bean.DrawableLayoutClickBean;

import java.util.List;

/**
 * Created by zxh on 2018/2/5.
 * 单条点击的Adapter
 */

public class DrawableLayoutClickAdapter {
    private CommonAdapter adapter;


    public CommonAdapter getAdapter(List<DrawableLayoutClickBean> dataList) {
        return getAdapter(dataList, R.layout.drawable_layout_click);
    }

    public CommonAdapter getAdapter(List<DrawableLayoutClickBean> dataList, int layoutID) {

        adapter = new CommonAdapter<DrawableLayoutClickBean>(Utils.getContext(), dataList, layoutID) {
            @Override
            public void convert(ViewHolder helper, DrawableLayoutClickBean item) {
                ImageView imageView_left = helper.getView(R.id.imageView_left);
                TextView textView_value = helper.getView(R.id.textView_value);
                ImageView imageView_right = helper.getView(R.id.imageView_right);

                if (!TextUtils.isEmpty(item.getKey())) {

                    if (item.getDrawableLeft() != null) {
                        imageView_left.setImageDrawable(item.getDrawableLeft());
                        imageView_left.setVisibility(View.VISIBLE);
                    } else {
                        imageView_left.setVisibility(View.GONE);
                    }

                    if (item.getValue() != null) {
                        textView_value.setText(item.getValue());
                    } else {
                        textView_value.setText("");
                    }

                    if (item.getDrawableRight() != null) {
                        imageView_right.setImageDrawable(item.getDrawableRight());
                        imageView_right.setVisibility(View.VISIBLE);
                    } else {
                        //donoting
                        imageView_right.setVisibility(View.VISIBLE);
                    }

                } else {
                    LogUtils.i("DrawableLayoutClickAdapter-->key值不能为空");
                }
            }
        };
        return adapter;
    }
}
