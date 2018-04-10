package com.qiang.manicurists.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.GoodsActivity;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.activity.NewOrHotActivity;
import com.qiang.manicurists.activity.SearchActivity;
import com.qiang.manicurists.adapter.CategoryGoodsGridviewAdapter;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.ViewUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private View rootview;

    //toolbar相关的控件
    private Toolbar category_toolbar;
    //radiogroup相关的控件，商品的分类
    private RadioGroup category_radiogroup;
    //商品详细列表的相关控件
    private LinearLayout category_contentview, category_loading;
    private ImageView top_img;
    private TextView top_text;
    private GridView category_goods_gridview;
    private ArrayList<Goods> goods_gridview_img_list;

    private int width = LaunchActivity.windows_width;
    private int radiogroup_width = 0;

    //虚拟的数据
    private String[] movie_name = {"美甲", "美足", "美容", "美睫", "造型", "美发", "健身", "充值"};

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootview == null) {
            ArrayList<Goods> goods_list = (ArrayList<Goods>) getArguments().getSerializable("goods_list");
            rootview = inflater.inflate(R.layout.fragment_category, container, false);
            initToolbar(rootview);
            initRadiogroup(rootview);
            initContent(rootview, goods_list);
        }
        if (container != null) {
            container.removeView(rootview);
        }
        return rootview;
    }

    private void initToolbar(View rootview) {
        category_toolbar = (Toolbar) rootview.findViewById(R.id.category_toolbar_id);
        ViewUtil.initAfterSetContentView(getActivity(), category_toolbar);
        LinearLayout category_search = (LinearLayout) rootview.findViewById(R.id.category_search_id);
        category_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.GoIntent(getActivity(), SearchActivity.class);
            }
        });
    }

    private void initContent(final View rootview, final ArrayList<Goods> goods_list) {
        category_loading = (LinearLayout) rootview.findViewById(R.id.category_loading_linear_id);
        //加载的那个小动画
        ImageView loading_img = (ImageView) rootview.findViewById(R.id.category_loading_anim_id);
        AnimationDrawable anim_loading = (AnimationDrawable) loading_img.getDrawable();
        anim_loading.start();

        //加载右边的内容使徒
        ((RadioButton) category_radiogroup.getChildAt(0)).setChecked(true);//因为切换radio的时候会对loading界面进行才做，所以放这里
        top_img = (ImageView) rootview.findViewById(R.id.category_contentview_topimg_id);
        top_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.cats));
        top_text = (TextView) rootview.findViewById(R.id.category_contentview_toptext_id);
        top_text.setText("全部" + movie_name[0] + ">>");
        top_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewOrHotActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "商品");
                bundle.putSerializable("goods_list", (Serializable) goods_list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //获取radiogroup的宽度
        ViewTreeObserver vto = category_radiogroup.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                category_radiogroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                category_radiogroup.getHeight();
                radiogroup_width = category_radiogroup.getWidth();

                category_contentview = (LinearLayout) rootview.findViewById(R.id.category_contentview_linear_id);
                for (int i = 0; i < goods_list.size(); i++) {
                    View goods_view = LayoutInflater.from(getContext()).inflate(R.layout.layout_category_goods_item, null);
                    TextView goods_title = (TextView) goods_view.findViewById(R.id.category_goods_title_id);
                    goods_title.setText(goods_list.get(i).getGoodsName());
                    category_goods_gridview = (GridView) goods_view.findViewById(R.id.category_goods_gridview_id);
                    CategoryGoodsGridviewAdapter adapter = new CategoryGoodsGridviewAdapter(getContext(), goods_list, (width - radiogroup_width) / 3);
                    category_goods_gridview.setAdapter(adapter);
                    category_goods_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            BaseUtil.GoIntent(getActivity(), GoodsActivity.class, "goods", (Serializable) parent.getAdapter().getItem(position));
                        }
                    });
                    category_contentview.addView(goods_view);
                }
            }
        });
        //
    }

    private void initRadiogroup(View rootview) {
        category_radiogroup = (RadioGroup) rootview.findViewById(R.id.category_radiogroup_id);
        for (int i = 0; i < movie_name.length; i++) {
            View radio = LayoutInflater.from(getContext()).inflate(R.layout.layout_category_radiogroup_item, null);
            ((RadioButton) radio).setText(movie_name[i]);
            category_radiogroup.addView(radio, width / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        category_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                category_loading.setVisibility(View.VISIBLE);
                category_loading.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        category_loading.setVisibility(View.GONE);
                    }
                }, 2000);
                for (int i = 0; i < movie_name.length; i++) {
                    RadioButton radiobutton = (RadioButton) category_radiogroup.getChildAt(i);
                    if (radiobutton.getId() == checkedId) {
                        String choose_text = radiobutton.getText().toString();
                        updatecontentview(checkedId, choose_text);
                        break;
                    }
                }
            }
        });
    }

    private void updatecontentview(int checkedId, String choose_text) {
        if (top_text == null) return;
        top_text.setText("全部" + choose_text + ">>");
        //修改商品的内容
    }

}
