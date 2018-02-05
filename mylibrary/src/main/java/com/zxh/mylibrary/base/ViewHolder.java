package com.zxh.mylibrary.base;

/**
 * Created by zxh
 */
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewHolder通用类
 */
public class ViewHolder {

    private final SparseArray<View> views;
    private View convertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {

        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);

        //setTag
        convertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return ViewHolder
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {

        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }

        return (ViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return View
     */
    public <T extends View> T getView(int viewId) {

        View view = views.get(viewId);

        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }

        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

}
