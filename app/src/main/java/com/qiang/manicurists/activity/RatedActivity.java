package com.qiang.manicurists.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.GoodsRatedGridviewAdapter;
import com.qiang.manicurists.adapter.RatedRecyclerviewAdapter;
import com.qiang.manicurists.bean.Rated;
import com.qiang.manicurists.util.OrderUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.LoadMoreRecyclerView;

import java.util.ArrayList;

public class RatedActivity extends AppCompatActivity {
    //recyclerview相关的控件
    private LoadMoreRecyclerView rated_recyclerview;
    private RatedRecyclerviewAdapter adapter;
    private ArrayList<Rated> rated_list_keeping;

    //虚拟的数据
    private String[] movie_name = {"全部","好评","中评","差评"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rated);
        ArrayList<Rated> rated_list = (ArrayList<Rated>) getIntent().getSerializableExtra("rated_list");
        rated_list_keeping = new ArrayList<Rated>();
        rated_list_keeping.addAll(rated_list);
        String title_text = getIntent().getStringExtra("title");
        initTopbar(title_text);
        initGroupradio(rated_list);
        initRecycler(rated_list);
    }

    private void initRecycler(final ArrayList<Rated> rated_list) {
        rated_recyclerview = (LoadMoreRecyclerView) findViewById(R.id.rated_recyclerview_id);
        View headview = new View(this);
        View footview = LayoutInflater.from(this).inflate(R.layout.layout_footview,null);
        rated_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RatedRecyclerviewAdapter(rated_list, new RatedRecyclerviewAdapter.onitemclickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录下点击的图片的位置和大小
                int[] screenLocation = new int[2];
                ImageView imageview = (ImageView) view.findViewById(R.id.rated_pic_id);
                imageview.getLocationOnScreen(screenLocation);
                Intent intent = new Intent(RatedActivity.this,PhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putSerializable("rated_url_list", ((GoodsRatedGridviewAdapter)parent.getAdapter()).getPicList());
                bundle.putInt("left", screenLocation[0]);
                bundle.putInt("top", screenLocation[1]);
                bundle.putInt("width", imageview.getWidth());
                bundle.putInt("height", imageview.getHeight());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_push_in,R.anim.anim_activity_push_out);
            }
        });
        rated_recyclerview.setAdapter(adapter);
        adapter.setHeaderView(headview);
        adapter.setFootView(footview);

        rated_recyclerview.setAutoLoadMoreEnable(true);
        rated_recyclerview.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.addGoodsDatas(rated_list);
                rated_list_keeping.addAll(rated_list);
                rated_recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rated_recyclerview.setLoadingMore(false);
                    }
                }, 1000);
            }

        });
    }

    private void initGroupradio(final ArrayList<Rated> rated_list) {
        RadioGroup headerview_radiogroup = (RadioGroup) this.findViewById(R.id.rated_radiogroup_id);
        for (int i = 0;i<movie_name.length;i++){
            View radio = LayoutInflater.from(this).inflate(R.layout.layout_rated_radiogroup_item,null);
            ((RadioButton) radio).setText(movie_name[i]);
            headerview_radiogroup.addView(radio, LaunchActivity.windows_width/movie_name.length, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        headerview_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0 ;i<movie_name.length;i++){
                    RadioButton radiobutton = (RadioButton) group.getChildAt(i);
                    if(radiobutton.getId() == checkedId){
                        String choose_text = radiobutton.getText().toString();
//                        BaseUtil.ShowToast(getApplicationContext(),"切换recyclerview的数据"+choose_text);
                        if(adapter!=null) {
                            if (i == 0) {
                                adapter.reSetGoodsDatas(rated_list_keeping);
                            } else {
                                adapter.reSetGoodsDatas(OrderUtil.OrderForRated(rated_list_keeping, i-1 + ""));
                            }
                        }
                        break;
                    }
                }
            }
        });
        ((RadioButton)headerview_radiogroup.getChildAt(0)).setChecked(true);
    }

    private void initTopbar(String title_text) {
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
}
