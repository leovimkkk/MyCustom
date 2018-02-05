package com.zxh.mycustom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.xwjr.utilcode.utils.AppUtils;
import com.xwjr.utilcode.utils.LogUtils;
import com.xwjr.utilcode.utils.Utils;
import com.zxh.mylibrary.base.CommonAdapter;
import com.zxh.mylibrary.bean.DrawableLayoutClickBean;
import com.zxh.mylibrary.manager.DrawableLayoutClickManager;
import com.zxh.mylibrary.adapter.DrawableLayoutClickAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private CommonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**初始化工具类*/
        Utils.init(this);

        /**初始化Log工具类*/
        LogUtils.init(AppUtils.isAppDebug(this), false, 'v', "zxh");
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);




        List<DrawableLayoutClickBean> dataList = new ArrayList<>();
        DrawableLayoutClickManager manager = new DrawableLayoutClickManager(dataList);
        manager.add("key1","keykeykey1",getResources().getDrawable(R.mipmap.assets_item1));
        manager.add("key2","keykeykey2",getResources().getDrawable(R.mipmap.assets_item1));
        manager.add("key2","keykeykey2",getResources().getDrawable(R.mipmap.assets_item1));
        listview.setAdapter(new DrawableLayoutClickAdapter().getAdapter(dataList,R.layout.drawable_layout_click));
    }
}
