package com.zxh.mylibrary.base;

/**
 * Created by zxh
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Adapter基类
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected LayoutInflater inflater;

    protected Context context;

    protected List<T> dataList;

    protected final int itemLayoutId;

    public CommonAdapter(Context context, List<T> dataList, int itemLayoutId) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.dataList = dataList;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return dataList != null ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position));

        return viewHolder.getConvertView();
    }

    /**
     * 获取ViewHolder
     *
     * @return ViewHolder
     */
    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(context, convertView, parent, itemLayoutId,
                position);
    }

    /**
     * 转换视图元素为控件
     *
     */
    public abstract void convert(ViewHolder helper, T item);

    /**
     * 获取数据集合
     *
     * @return List
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置数据集合
     *
     * @param dataList
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}

