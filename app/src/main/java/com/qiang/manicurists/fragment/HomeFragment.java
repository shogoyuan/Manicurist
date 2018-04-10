package com.qiang.manicurists.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.GoodsActivity;
import com.qiang.manicurists.activity.GoodsListActivity;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.activity.NewOrHotActivity;
import com.qiang.manicurists.activity.SearchActivity;
import com.qiang.manicurists.adapter.HomeGoodsGridviewAdapter;
import com.qiang.manicurists.adapter.HomeGridviewAdapter;
import com.qiang.manicurists.adapter.HomeViewpagerAdapter;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.handle.HomeViewpagerHandle;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.MyScrollView;
import com.qiang.manicurists.view.PullToRefreshView;
import com.qiang.manicurists.view.viewpagerutil.DepthPageTransformer;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View rootview;
    //pulltorefresh相关的控件
    private PullToRefreshView home_pulltorefresh;
    private RelativeLayout home_rel;

    //toolbar相关的控件
    private Toolbar home_toolbar;
    private LinearLayout home_search;
    private MyScrollView home_scrollview;

    //viewpager相关的控件
    public ViewPager home_viewpager;
    private ArrayList<Integer> viewpager_img_list;
    private LinearLayout home_viewpager_linear;
    private CheckBox last_Btn;

    //gridview相关的控件
    private GridView home_gridview;
    private ArrayList<String> gridview_name_list;
    private ArrayList<Integer> gridview_img_list;
    private Integer[] movie_int = {R.mipmap.icon_top_tab_bar_meijia,R.mipmap.icon_top_tab_bar_youhuiquan,
            R.mipmap.icon_top_tab_bar_meirong, R.mipmap.icon_top_tab_bar_meijie,
            R.mipmap.icon_top_tab_bar_meizhuang,R.mipmap.icon_top_tab_bar_meifa,
            R.mipmap.icon_top_tab_bar_jianshen,R.mipmap.icon_top_tab_bar_chongzhi};
    private String[] movie_name = {"美甲","美足","美容","美睫","造型","美发","健身","充值"};

    //promotions相关控件
    private ArrayList<ImageView> home_promotions_img_list;


    //goods相关控件
    private LinearLayout home_goods_linear;
    private LinearLayout home_goods_item_linear;
    private LinearLayout home_goods_more;
    private TextView home_goods_title_text,home_goods_more_text;
    private GridView home_goods_gridview;
    private String[] goods_title = {"今日上新","热门美容","热门美甲"};

    //相关的数据控件
//    ArrayList<Goods> goods_list;


    public HomeViewpagerHandle handler = new HomeViewpagerHandle(new WeakReference<HomeFragment>(this));

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootview == null) {
            ArrayList<Goods> goods_list = (ArrayList<Goods>) getArguments().getSerializable("goods_list");
            rootview = inflater.inflate(R.layout.fragment_home, container, false);
            initToolbar(rootview);
            initPulltorefresh(rootview);
            initView(rootview,goods_list);
        }
        if (container != null) {
            container.removeView(rootview);
        }
        return rootview;
    }

    /*
    初始化主页上的scrollview里面控件的基本操作
    */
    private void initView(View rootview, ArrayList<Goods> goods_list) {
        initViewpager(rootview);
        initGridview(rootview,goods_list);
        initPromotions(rootview);
        initGoods(rootview,goods_list);
    }

    private void initGoods(View rootview, final ArrayList<Goods> goods_list) {
        home_goods_linear = (LinearLayout) rootview.findViewById(R.id.home_goods_linear_id);
        for (int i = 0; i < 3; i++) {
            View goods_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_goods_item, null);
            home_goods_item_linear = (LinearLayout) goods_view.findViewById(R.id.home_goods_item_linear_id) ;
            home_goods_gridview = (GridView) goods_view.findViewById(R.id.home_goods_gridview_id);
            home_goods_title_text = (TextView) goods_view.findViewById(R.id.home_goods_title_text_id);
            home_goods_more_text = (TextView) goods_view.findViewById(R.id.home_goods_more_text_id);
            if(i == 0){
                home_goods_item_linear.setBackgroundColor(Color.BLACK);
                home_goods_title_text.setTextColor(getResources().getColor(R.color.title_text_color));
                home_goods_more_text.setTextColor(getResources().getColor(R.color.title_text_color));
            }
            home_goods_title_text.setText(goods_title[i]);

            HomeGoodsGridviewAdapter adapter = new HomeGoodsGridviewAdapter(getContext(), goods_list,i, LaunchActivity.windows_width/3);
            home_goods_gridview.setAdapter(adapter);
            home_goods_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BaseUtil.GoIntent(getActivity(),GoodsActivity.class,"goods",(Serializable)goods_list.get(position));
                }
            });

            home_goods_more = (LinearLayout) goods_view.findViewById(R.id.home_goods_more_id);
            final int finalI = i;
            home_goods_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewOrHotActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",goods_title[finalI]);
                    bundle.putSerializable("goods_list",(Serializable) goods_list);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            home_goods_linear.addView(goods_view);
        }
    }

    private void initPromotions(View rootview) {
        home_promotions_img_list = new ArrayList<ImageView>();
        home_promotions_img_list.add((ImageView) rootview.findViewById(R.id.home_promotions_img1_id));
        home_promotions_img_list.add((ImageView) rootview.findViewById(R.id.home_promotions_img2_id));
        home_promotions_img_list.add((ImageView) rootview.findViewById(R.id.home_promotions_img3_id));
        Glide.with(getContext()).load(R.mipmap.cats).diskCacheStrategy(DiskCacheStrategy.ALL).into(home_promotions_img_list.get(0));
        Glide.with(getContext()).load(R.mipmap.picture1).diskCacheStrategy(DiskCacheStrategy.ALL).into(home_promotions_img_list.get(1));
        Glide.with(getContext()).load(R.mipmap.picture2).diskCacheStrategy(DiskCacheStrategy.ALL).into(home_promotions_img_list.get(2));
    }

    private void initGridview(View rootview, final ArrayList<Goods> goods_list) {
        home_gridview = (GridView) rootview.findViewById(R.id.home_gridview_id);
        gridview_img_list = new ArrayList<Integer>();
        gridview_name_list = new ArrayList<String>();
        for (int i = 0; i < movie_int.length; i++) {
            gridview_img_list.add(movie_int[i]);
            gridview_name_list.add(movie_name[i]);
        }
        HomeGridviewAdapter adapter = new HomeGridviewAdapter(getContext(), gridview_img_list,gridview_name_list);
        home_gridview.setAdapter(adapter);
        BaseUtil.setgridviewHeightBasedOnChildren(home_gridview,4);
        home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GoodsListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",movie_name[position]);
                bundle.putSerializable("goods_list",(Serializable) goods_list);
                intent.putExtras(bundle);
                startActivity(intent);
//                BaseUtil.GoIntent(getActivity(), GoodsListActivity.class,"goods_list",(Serializable) goods_list);
            }
        });
    }

    private void initViewpager(View rootview) {
        home_viewpager = (ViewPager) rootview.findViewById(R.id.home_viewpager_id);
        BaseUtil.setViewPagerScrollSpeed(home_viewpager,1000);//修改切换动画的速度
        home_viewpager.setPageTransformer(true,new DepthPageTransformer());
//        home_viewpager.setOffscreenPageLimit(3);//预加载
        viewpager_img_list = new ArrayList<Integer>();
        viewpager_img_list.add(R.mipmap.picture1);
        viewpager_img_list.add(R.mipmap.picture2);
        HomeViewpagerAdapter adapter = new HomeViewpagerAdapter(getContext(), viewpager_img_list);
        home_viewpager.setAdapter(adapter);

        home_viewpager_linear = (LinearLayout) rootview.findViewById(R.id.home_viewpager_cyclelinear_id);
        for (int i = 0; i < viewpager_img_list.size(); i++) {
           View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_viewpager_cycle_item,null);
            home_viewpager_linear.addView(view,10,10);
            //要先addview后才能获取到paeams
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (i == 0) {
                last_Btn = (CheckBox) view;
                last_Btn.setChecked(true);
                params.setMargins(0, 0, 0, 0);
            }else{
                params.setMargins(10, 0, 0, 0);
            }
            view.setLayoutParams(params);
        }

        home_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.sendMessage(Message.obtain(handler, HomeViewpagerHandle.MSG_PAGE_CHANGED, position, 0));
                if (last_Btn != null) last_Btn.setChecked(false);
                CheckBox current_btn = (CheckBox) home_viewpager_linear.getChildAt(position % (viewpager_img_list.size()));
                current_btn.setChecked(true);
                last_Btn = current_btn;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(HomeViewpagerHandle.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(HomeViewpagerHandle.MSG_UPDATE_IMAGE, HomeViewpagerHandle.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        home_viewpager.setCurrentItem(0);//默认在中间，使用户看不到边界
        //开始轮播效果
        handler.sendEmptyMessageDelayed(HomeViewpagerHandle.MSG_UPDATE_IMAGE, HomeViewpagerHandle.MSG_DELAY);

    }

    /*
        初始化下拉刷新的基本操作
        */
    private void initPulltorefresh(View rootview) {
        home_pulltorefresh = (PullToRefreshView) rootview.findViewById(R.id.home_pulltorefreshview_id);
        home_pulltorefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //在这里获取数据的刷新
                //设置一个延时操作来停止刷新滑动
//                getData(CacheType.NETWORK_ELSE_CACHED);

                home_pulltorefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        home_pulltorefresh.setRefreshing(false);
//                        BaseUtil.ShowToast(getContext(),"刷新结束");
                    }
                }, 3000);//3秒后结束动画
            }
        });
    }

    /*
    初始化主页上的toolbar的基本操作
    */
    private void initToolbar(View rootview) {
        final int alpha_height = LaunchActivity.windows_width;
        home_toolbar = (Toolbar) rootview.findViewById(R.id.home_toolbar_id);
        ViewUtil.initAfterSetContentView(getActivity(),home_toolbar);
        home_toolbar.getBackground().setAlpha(0);//让背景先变成透明
        home_search = (LinearLayout) rootview.findViewById(R.id.home_search_id);
        home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.GoIntent(getActivity(), SearchActivity.class);
            }
        });
        home_scrollview = (MyScrollView) rootview.findViewById(R.id.home_scrollview_id);
        home_scrollview.setScrollviewlistener(new MyScrollView.onScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y < alpha_height) {
                    float alpha = (float) y /alpha_height;
                    if (alpha >= 0) {
                       home_toolbar.getBackground().setAlpha((int) (alpha * 255));
                    }
                }
            }
        });
    }

}
