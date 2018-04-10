package com.qiang.manicurists.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qiang.manicurists.R;
import com.qiang.manicurists.bean.Craftsmen;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.bean.HttpResult;
import com.qiang.manicurists.bean.Rated;
import com.qiang.manicurists.bean.Rated_Url;
import com.qiang.manicurists.http.HttpTest;
import com.qiang.manicurists.http.OKHttpUtils;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.ViewUtil;

import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;

public class LaunchActivity extends AppCompatActivity {
    private LinearLayout launch_linear;
    private ImageView launch_img;//启动页面的APP图标
    private ImageView loading_img;//下面的加载中图标

    public static OKHttpUtils okHttpUtils;

    private int[] icon = {R.mipmap.pigu1,R.mipmap.pigu2,R.mipmap.pigu3,R.mipmap.pigu4,R.mipmap.pigu5,
            R.mipmap.pigu6,R.mipmap.pigu7,R.mipmap.pigu8,R.mipmap.pigu9,R.mipmap.pigu10,
            R.mipmap.pigu11,R.mipmap.pigu12,R.mipmap.pigu13,R.mipmap.pigu14,R.mipmap.pigu15,
            R.mipmap.pigu16,R.mipmap.pigu17,R.mipmap.pigu18,R.mipmap.pigu19};

    public static int[] rated_id = {R.mipmap.star1,R.mipmap.star2,R.mipmap.star3,R.mipmap.star4,
            R.mipmap.star5,R.mipmap.star6,R.mipmap.star7,R.mipmap.star8, R.mipmap.star9,R.mipmap.star10,};

    //屏幕宽度
    public static int windows_width;
    public static int windows_height;

    private Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ViewUtil.initAfterSetContentView(this,null);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        windows_width = metric.widthPixels;     // 屏幕宽度（像素）
        windows_height= metric.heightPixels;
        okHttpUtils  = new OKHttpUtils.Builder(getApplicationContext()).build();
        setAnimation();
        initView();

    }

    HttpTest httpTest;
    HttpResult<Goods> goods_data;
    private void initData() {
        httpTest = new HttpTest(okHttpUtils);
        httpTest.getGoodsData(new HttpTest.HttpListener() {
            @Override
            public void onFailure(Call call, IOException e) {
                    setalldata();
            }

            @Override
            public void onResponse(Call call, Object object,int goods_id) {
                goods_data = ((HttpResult<Goods>) object);
                setalldata2(goods_data);
//                for (int i=0;i<3;i++) {
//                    BaseUtil.ShowLog("123", goods_data.getInfo().get(0).getGoodsName()
//                    +"     "+goods_data.getInfo().get(0).getGoodsRated().get(0).getRatedContent()
//                    +"      "+goods_data.getInfo().get(0).getGoodsCraftsmen().getCraftsmenName());
//                }
            }
        });
    }

    private void initView() {
        launch_linear = (LinearLayout) this.findViewById(R.id.launch_linear_id);
        launch_linear.setAnimation(anim);
        anim.start();

        loading_img = (ImageView) this.findViewById(R.id.launch_loading_img_id);
        AnimationDrawable anim_loading = (AnimationDrawable) loading_img.getDrawable();
        anim_loading.start();
    }

    private void setAnimation() {
        anim = new AlphaAnimation(0,1);
        anim.setDuration(2000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                initData();
                setalldata();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //虚拟的假数据
    private ArrayList<Goods> goods_array;
    private String[] goodsname = {"脸部护理","水晶甲","琉璃甲","美睫1","美睫2","美发1","美发2"};
    private String[] goodsurl = {"http://image.xinmin.cn/2013/03/14/20130314154620562655.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1440820578,4059810039&fm=116&gp=0.jpg",
            "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1467104911&di=52ebfc7959d3ba5d107ec49b946f2d66&src=http://www.88school.cn/ckfinder/userfiles/images/%E7%BE%8E%E7%94%B22(1).jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3372498095,1741299664&fm=116&gp=0.jpg",
            "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1467105054&di=3d8ad502742070871868094f7efd084a&src=http://p1.meituan.net/460.280/deal/87fc509000788de11c944f047affc9fc45690.jpg",
            "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1467257395&di=c290f1f4a3bf040e01f70ba7e205ed6b&src=http://pic55.nipic.com/file/20141204/12953641_191735716000_2.jpg",
            "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1467105130&di=e61198c2c31977ce1a72411cedf97b5a&src=http://img01.taopic.com/150127/240386-15012F93S459.jpg"};
    public void setalldata() {
        goods_array = new ArrayList<Goods>();
        for (int i = 0 ; i <goodsname.length;i++){
            Goods goods = new Goods();
            goods.setGoodsName(goodsname[i]);
            goods.setGoodsUrl(goodsurl[i]);
            goods.setGoodsPrice((50*i)+"蚊");//原价
            goods.setGoodsDiscount((50*i)+"蚊");
            goods.setGoodsLike(i+1+"");
            switch (i){
                case 0: goods.setGoodsContent("静静的，雪之下雪乃做出决意。\n" +
                        "因刺骨般的寒冷而醒了。\n" +
                        "“……好冷”\n" +
                        "从沙发上磨磨蹭蹭的起来后，毛毯轻轻的飘落了。\n" +
                        "看来昨晚好像就那样睡着了。确实有种被母亲零碎的念叨着什么的感觉。在那种地方睡着的话会感冒啊之类的事吧。");break;
                case 1: goods.setGoodsContent("可是，好像浪费了忠告依旧那样睡了呢。在隐约的记忆中有印象所以确实回答了什么吧，但是结果似乎还是在沙发上不经意间落入了睡眠。在一起的卡玛库拉也不知道在什么时候不见了。是到更温暖的某个地方去睡了吧。");break;
                case 2: goods.setGoodsContent("一边感觉头、肩膀和腰喀嚓喀嚓的挤压着一边站了起来。\n" +
                        "一眼看去早饭已经被在桌子上准备好了。\n" +
                        "吃完后在家中转了转，似乎双亲早已离开了家呢。并且，小町好像也已经上学了、留到最后的是我啊。");break;
                case 3: goods.setGoodsContent("在桌子上带回来的放着不管的多拿滋仿佛少了几个，姑且像是被谁吃了呢。\n" +
                        "换衣服时日益寒冷的空气侵染着身体。\n" +
                        "真的感冒了吗……还是由于用奇怪的姿势睡觉而没获得充分的休息呢。\n" +
                        "头也莫名的痛着。头痛药，有买好放着的吗……搜寻着橱柜、喝下了找到的药。");break;
                case 4: goods.setGoodsContent("恩哦哦哦哦哦！药小真腻害哦哦哦哦哦！（恩哦哦哦哦哦！药效真厉害哦哦哦哦哦！！）");break;
                case 5: goods.setGoodsContent("嗯，果然喝药的话不这么做不行哪。\n" +
                        "走出门，边不断重复的嘟囔着真冷啊真冷啊边骑着自行车，朝向了学校。\n" +
                        "也有昨天是度过修学旅行后的第一天的原因，在哪里存在着浮躁的气氛，但是那也在恢复正常上课后远远的消失到了某处。");break;
                case 6: goods.setGoodsContent("校门、停车场、升降口，至今为止看过的接近两年的光景在扩散着。尽管如此也没有感到亲近真是不可思议啊。\n" +
                        "进入升降口后，无意中碰到了由比滨。\n" +
                        "“啊……早、早上好”\n" +
                        "“啊啊”");break;
            }
//            ArrayList<String> time_list = new ArrayList<String>();
//            time_list.add("耗时120分钟");time_list.add("维持25天");
            goods.setGoodsTime("耗时120分钟,维持25天");

            ArrayList<String> recommend_list = new ArrayList<String>();
            recommend_list.add("可上门");recommend_list.add("明星推荐"); recommend_list.add("安全保障");
            goods.setGoodsRecommend(recommend_list);
            ArrayList<ArrayList<Integer>> booking_list = new ArrayList<>();
            for (int k = 0 ; k < 30;k++){
                ArrayList<Integer> int_list = new ArrayList<Integer>();
                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumIntegerDigits(1);
                for (int z = 0;z<16;z++) int_list.add(Math.random()>0.5?1:0);
                booking_list.add(int_list);
            }
            goods.setGoodsBooking(booking_list);

//            ArrayList<String> serviceconfines_list = new ArrayList<String>();
//            for (int g = 0;g<20;g++) {
//                serviceconfines_list.add("地点"+i);
//                serviceconfines_list.add("地点2"+i);
//            }
            goods.setGoodsServiceConfines("地点1,地点2");

            //等下继续敲评价
            ArrayList<Rated> rated_list = new ArrayList<Rated>();
            for(int a = 0;a<7;a++){
                Rated rated = new Rated();
                rated.setRatedIcon(icon[new Random().nextInt(10)]+"");
                rated.setRatedNum(a+"号码");
                rated.setRatedLevel(new Random().nextInt(10)+"");
                switch (a){
                    case 0:
                        rated.setRatedContent("为什么会变成这样呢……第一次有了喜欢的人。有了能做一辈子朋友的人。两件快乐事情重合在一起。而这两份快乐，又给我带来更多的快乐。得到的，本该是像梦境一般幸福的时间……但是，为什么，会变成这样呢……");
                        ArrayList<Rated_Url> ratedpicurl = new ArrayList<>();
                        for (int m=0;m<4;m++) {
                            Rated_Url rated_url = new Rated_Url();
                            rated_url.setUrl(goodsurl[m]);
                            ratedpicurl.add(rated_url);
                        }
                        rated.setRatedPicUrl(ratedpicurl);
                        break;
                    case 1: rated.setRatedContent("是我，是我先，明明都是我先来的……接吻也好，拥抱也好，还是喜欢上那家伙也好");break;
                    case 2: rated.setRatedContent("为什么你会这么熟练啊！你和雪菜亲过多少次了啊！？你到底要把我甩开多远你才甘心啊！？");break;
                    case 3: rated.setRatedContent("为什么会变成这样呢…喜欢的他，不惜对我撒谎，向我转过身来。不惜舍弃自己的思念，向我补偿一切。跨越三年的感情，终于实现了。那样本应该已经满足了。只要我的心愿实现，应该就能和好了。但，为什么，会变成这样呢…\n");break;
                    case 4: rated.setRatedContent("明明遥不可及，却又近在咫尺，先想出这种拷问方式的不就是你吗！");break;
                    case 5: rated.setRatedContent("你什么时候变得这么熟练了，你跟雪菜KSS了多少次啊！？你们两个到底想甩下我多远！？");break;
                    case 6: rated.setRatedContent("武也：哦，最近都改成网上下载了，让我看看你的收藏夹。\n" +
                            "春希：住手啊！\n" +
                            "yio：春希。。。");break;
                }
                rated.setRatedDate(a+"日期");
                rated_list.add(rated);
            }
            goods.setGoodsRated(rated_list);

            //手艺人
            Craftsmen craftsmen = new Craftsmen();
            craftsmen.setCraftsmenIcon(icon[new Random().nextInt(10)+9]+"");
            craftsmen.setCraftsmenName("无名");
            craftsmen.setCraftsmenPosition("专业水泥工");
            craftsmen.setCraftsmenLevel(new Random().nextInt(10)+"");//1~10
            goods.setGoodsCraftsmen(craftsmen);
            goods_array.add(goods);

            goods_data = new HttpResult<Goods>();
            goods_data.setInfo(goods_array);
        }
        launch_handle.sendEmptyMessage(1);
    }

    public void setalldata2(HttpResult<Goods> goods_data) {
        ArrayList<Goods> goods_list = goods_data.getInfo();
        for(int i=0;i<goods_list.size();i++ ) {
            ArrayList<String> recommend_list = new ArrayList<String>();
            recommend_list.add("可上门");
            recommend_list.add("明星推荐");
            recommend_list.add("安全保障");
            goods_list.get(i).setGoodsRecommend(recommend_list);
            ArrayList<ArrayList<Integer>> booking_list = new ArrayList<>();
            for (int k = 0; k < 30; k++) {
                ArrayList<Integer> int_list = new ArrayList<Integer>();
                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumIntegerDigits(1);
                for (int z = 0; z < 16; z++) int_list.add(Math.random() > 0.5 ? 1 : 0);
                booking_list.add(int_list);
            }
            goods_list.get(i).setGoodsBooking(booking_list);

            ArrayList<Rated> rated_list = goods_list.get(i).getGoodsRated();
            for (int j =0;j<rated_list.size();j++){
                Rated rated = rated_list.get(j);
                rated.setRatedIcon(icon[Integer.parseInt(rated.getRatedIcon())]+"");
            }
            Craftsmen craftsmen = goods_list.get(i).getGoodsCraftsmen();
            craftsmen.setCraftsmenIcon(icon[Integer.parseInt(craftsmen.getCraftsmenIcon())]+"");
        }
        launch_handle.sendEmptyMessage(1);
    }



    private Handler launch_handle = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == 1){
                //等服务器的连接来了后跳转就转到数据通信完毕的时候才执行跳转
                BaseUtil.GoIntent(LaunchActivity.this,MainActivity.class,"goods_list",(Serializable)goods_data.getInfo());
                finish();
            }
        }
    };
}
