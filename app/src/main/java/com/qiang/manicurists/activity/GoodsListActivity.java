package com.qiang.manicurists.activity;

import android.content.DialogInterface;
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
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.BaseRecyclerAdapter;
import com.qiang.manicurists.adapter.GoodsListRecyclerviewAdapter;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.dialog.GoodsListPopupWindows;
import com.qiang.manicurists.dialog.GoodsListSiftingsDialog;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.OrderUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.LoadMoreRecyclerView;
import com.qiang.manicurists.view.NestRadioGroup;

import java.util.ArrayList;

import static com.qiang.manicurists.view.LoadMoreRecyclerView.*;

public class GoodsListActivity extends AppCompatActivity {
    //recyclerview相关的控件
    private LoadMoreRecyclerView goodslist_recyclerview;
    private ArrayList<Goods> goods_list_keeping;
    private GoodsListRecyclerviewAdapter adapter;

    //headerview相关的控件
    private LinearLayout topview;
    private View headerview;
    private View footview;
    private NestRadioGroup radiogroup;
    private RadioButton radio1,radio2,radio3;

    private String[] order_str = {"销量由高到低","好评由高到低"};
    private String[] siftings_str = {"价格","时间"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodslist);
        ArrayList<Goods> goods_list = (ArrayList<Goods>) getIntent().getSerializableExtra("goods_list");
        goods_list_keeping = new ArrayList<Goods>();
        goods_list_keeping.addAll(goods_list);
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

    private boolean ismuch = false;
    private void initHeaderview(final ArrayList<Goods> goods_list) {
        topview = (LinearLayout)this.findViewById(R.id.goodslist_top_radiogroup_id);
        headerview = LayoutInflater.from(this).inflate(R.layout.layout_goodslist_headerview,null);
        footview = LayoutInflater.from(this).inflate(R.layout.layout_footview,null);
        radiogroup = (NestRadioGroup) topview.findViewById(R.id.goodslist_headerview_radiogroup_id);
        radio1 = (RadioButton) topview.findViewById(R.id.goodslist_headerview_radiobtn1_id);
        radio2 = (RadioButton) topview.findViewById(R.id.goodslist_headerview_radiobtn2_id);
        final ImageView radio2_img = (ImageView) topview.findViewById(R.id.goodslist_headerview_radiobtn2_img_id);//价格的箭头
        radio3 = (RadioButton) topview.findViewById(R.id.goodslist_headerview_radiobtn3_id);
        radiogroup.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, int checkedId) {
                //每次切换都重置一下goods_list
                adapter.reSetGoodsDatas(goods_list_keeping);
                //把价格排序的箭头图标改成未选中的状态
                if (!ismuch) {
                    radio2_img.setBackgroundResource(R.mipmap.ic_bangumi_detail_close);
                } else {
                    radio2_img.setBackgroundResource(R.mipmap.ic_bangumi_detail_open);
                }
            }
        });
        radio1.setText("综合");
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoodsListPopupWindows(getApplicationContext(), order_str, radio1,radio1.getText()+""
                        , new GoodsListPopupWindows.popupCheckListener() {
                    @Override
                    public void popupCheck(String text) {
                        if (text.length()>4) text.substring(0,4);
                        radio1.setText(text);
                        //排序操作
                        goodslist_recyclerview.smoothScrollToPosition(0);
                    }
                });
            }
        });
        radio2.setText("价格");
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ismuch){
                    radio2_img.setBackgroundResource(R.mipmap.ic_bangumi_detail_close2);
                    ismuch = false;
                    OrderUtil.OrderForPrice(adapter.getGoodsDatas(),OrderUtil.ORDER_UP);
                    adapter.notifyDataSetChanged();
                }else{
                    radio2_img.setBackgroundResource(R.mipmap.ic_bangumi_detail_open2);
                    ismuch = true;
                    OrderUtil.OrderForPrice(adapter.getGoodsDatas(),OrderUtil.ORDER_DOWN);
                    adapter.notifyDataSetChanged();
                }
                goodslist_recyclerview.smoothScrollToPosition(0);
            }
        });
        radio3.setText("筛选");
        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio3.setEnabled(false);
                GoodsListSiftingsDialog dialog = new GoodsListSiftingsDialog(GoodsListActivity.this, siftings_str,
                        new GoodsListSiftingsDialog.onConfirmListener() {
                    @Override
                    public void clickconfirm(int small_price, int big_price, String date, String time) {
//                        BaseUtil.ShowLog("123",small_price+"   "+big_price+"    "+date+"   "+time);
                        adapter.reSetGoodsDatas(OrderUtil.OrderForSiftings(goods_list_keeping,small_price,big_price,date,time));
                        goodslist_recyclerview.smoothScrollToPosition(0);
                    }
                });
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        radio3.setEnabled(true);
                    }
                });
            }
        });
    }

    private void initRecycler(final ArrayList<Goods> goods_list) {
        goodslist_recyclerview = (LoadMoreRecyclerView) this.findViewById(R.id.goodslist_recyclerview_id);
        goodslist_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        adapter = new GoodsListRecyclerviewAdapter(goods_list,LaunchActivity.windows_width/2);
        adapter.setHeaderView(headerview);
        adapter.setFootView(footview);
        goodslist_recyclerview.setTopheadView(topview);
        goodslist_recyclerview.setAdapter(adapter);
        goodslist_recyclerview.addItemDecoration(new ViewUtil.SpaceItemDecoration(10));//为recycler添加间距
        goodslist_recyclerview.setAutoLoadMoreEnable(true);
        goodslist_recyclerview.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.addGoodsDatas(goods_list);
                goods_list_keeping.addAll(goods_list);//在原基础上再添加数据，保存全部goods的信息
                goodslist_recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goodslist_recyclerview.setLoadingMore(false);
                    }
                }, 1000);
            }

        });
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
//                BaseUtil.ShowToast(getApplicationContext(),"点击"+position+"   "+((Goods)data).getGoodsName());
                BaseUtil.GoIntent(GoodsListActivity.this,GoodsActivity.class,"goods",(Goods)data);
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
