package com.qiang.manicurists.activity;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.qiang.manicurists.R;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.fragment.CategoryFragment;
import com.qiang.manicurists.fragment.HomeFragment;
import com.qiang.manicurists.util.BaseUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost main_tabhost;

    //tabs的数组
    private String tabs_names[] = {"首页", "分类"};
    private int tabs_img[] = {R.drawable.tabs_home_click_style, R.drawable.tabs_category_click_style};
    private Class tabs_fragments[] = {HomeFragment.class, CategoryFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Goods> goods_list = (ArrayList<Goods>) getIntent().getExtras().getSerializable("goods_list");

        initTabHost(goods_list);
    }


    private void initTabHost(ArrayList<Goods> goods_list) {
        main_tabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        main_tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //设置tabs卡的内容
        int count = tabs_fragments.length;
        for (int i = 0; i < count; i++) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("goods_list", goods_list);
            TabHost.TabSpec tabSpec = main_tabhost.newTabSpec(tabs_names[i]).setIndicator(getTabItemView(i));
            main_tabhost.addTab(tabSpec, tabs_fragments[i], bundle);
            //去掉分割线
            main_tabhost.getTabWidget().setDividerDrawable(null);
            //设置tabs的背景
//            main_tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.ic_launcher);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tabs_item,null);

        ImageView imageView = (ImageView) view.findViewById(R.id.tabs_img_id);
        imageView.setImageResource(tabs_img[index]);

//        TextView textView = (TextView) view.findViewById(R.id.tabs_text_id);
//        textView.setText(tabs_names[index]);

        return view;
    }


    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                BaseUtil.ShowToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
