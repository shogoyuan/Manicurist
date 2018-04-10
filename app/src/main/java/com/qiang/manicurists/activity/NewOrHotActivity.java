package com.qiang.manicurists.activity;

import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.BaseRecyclerAdapter;
import com.qiang.manicurists.adapter.NeworhotRecyclerviewAdapter;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.LoadMoreRecyclerView;

import java.util.ArrayList;

public class NewOrHotActivity extends AppCompatActivity {
    //recyclerview相关的控件
    private LoadMoreRecyclerView neworhot_recyclerview;
//    private ArrayList<Goods> goods_list;
    private NeworhotRecyclerviewAdapter adapter;

    //headerview相关的控件
    private LinearLayout topview,neworhot_loading;
    private View headerview;
    private View footview;

    //虚拟的数据
    private String[] movie_name = {"美甲","美足","美容","美睫","造型","美发","健身","充值"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworhot);
        ArrayList<Goods> goods_list = (ArrayList<Goods>) getIntent().getSerializableExtra("goods_list");
        String title_text = getIntent().getStringExtra("title");
        initToolbar(title_text);
        initHeaderview(goods_list);
        initRecycler(goods_list);
    }

    private void initToolbar(String title_text) {
        Toolbar neworhot_toolebar = (Toolbar) this.findViewById(R.id.back_toolbar_id);
        ViewUtil.initAfterSetContentView(this,neworhot_toolebar);
        TextView title = (TextView) this.findViewById(R.id.toolbar_back_title_id);
        title.setText(title_text);
        Button back = (Button) this.findViewById(R.id.toolbar_back_btn_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initHeaderview(ArrayList<Goods> goods_list) {
        neworhot_loading = (LinearLayout) findViewById(R.id.neworhot_loading_linear_id);
        //加载的那个小动画
        ImageView loading_img = (ImageView) findViewById(R.id.neworhot_loading_anim_id);
        AnimationDrawable anim_loading = (AnimationDrawable) loading_img.getDrawable();
        anim_loading.start();

        topview = (LinearLayout) this.findViewById(R.id.neworhot_top_radiogroup_id);
        headerview = LayoutInflater.from(this).inflate(R.layout.layout_neworhot_headerview,null);
        footview = LayoutInflater.from(this).inflate(R.layout.layout_footview,null);
        neworhot_recyclerview = (LoadMoreRecyclerView) this.findViewById(R.id.neworhot_recyclerview_id);//因为等下需要用到，所以把他提上来
        ImageView headerview_img = (ImageView) topview.findViewById(R.id.neworhot_headerview_img_id);
        headerview_img.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.cats));
        RadioGroup headerview_radiogroup = (RadioGroup) topview.findViewById(R.id.neworhot_headerview_radiogroup_id);
        for (int i = 0;i<goods_list.size();i++){
            View radio = LayoutInflater.from(this).inflate(R.layout.layout_neworhot_radiogroup_item,null);
            ((RadioButton) radio).setText(movie_name[i]);
            headerview_radiogroup.addView(radio, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        headerview_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0 ;i<movie_name.length;i++){
                    RadioButton radiobutton = (RadioButton) group.getChildAt(i);
                    if(radiobutton.getId() == checkedId){
                        String choose_text = radiobutton.getText().toString();
//                        BaseUtil.ShowToast(getApplicationContext(),"切换recyclerview的数据"+choose_text);
                        if(neworhot_recyclerview != null)
                            neworhot_recyclerview.smoothScrollToPosition(0);

                        neworhot_loading.setVisibility(View.VISIBLE);
                        if(neworhot_recyclerview!=null)  neworhot_recyclerview.setCanScroller(false);
                        neworhot_loading.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                neworhot_loading.setVisibility(View.GONE);
                                if(neworhot_recyclerview!=null)  neworhot_recyclerview.setCanScroller(true);
                            }
                        },2000);
                        break;
                    }
                }
            }
        });
        //暂时不清楚第0位是那个控件
        ((RadioButton)headerview_radiogroup.getChildAt(1)).setChecked(true);
    }

    private void initRecycler(final ArrayList<Goods> goods_list) {
        neworhot_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));//2列，垂直排序
        adapter = new NeworhotRecyclerviewAdapter(goods_list,LaunchActivity.windows_width/2);
        adapter.setHeaderView(headerview);
        adapter.setFootView(footview);
        neworhot_recyclerview.setTopheadView(topview);
        neworhot_recyclerview.setAdapter(adapter);
        neworhot_recyclerview.addItemDecoration(new ViewUtil.SpaceItemDecoration(10));//为recycler添加间距
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                BaseUtil.GoIntent(NewOrHotActivity.this,GoodsActivity.class,"goods",(Goods) data);
            }
        });
        neworhot_recyclerview.setAutoLoadMoreEnable(true);
        neworhot_recyclerview.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.addGoodsDatas(goods_list);
                neworhot_recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        neworhot_recyclerview.setLoadingMore(false);
                    }
                }, 1000);
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK
                && event.getRepeatCount() == 0){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
