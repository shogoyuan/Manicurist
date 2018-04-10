package com.qiang.manicurists.activity;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.RatedEditGridviewAdapter;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.bean.Rated_Url;
import com.qiang.manicurists.db.SharePreferenceUtil;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.BtnUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.morphingbutton.IndeterminateProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RatedEditActivity extends AppCompatActivity {
    private final int REQUEST_CAMERA = 1;
    private final int REQUEST_PICTURE = 0;
    private String photoPath, photoName;//保存的图片参数
    private BtnUtil btn_util;
    private MaterialEditText MEditText;
    private LinearLayout img_linear;
    private IndeterminateProgressButton goods_buy_btn;
    private GridView rated_gridview;
    private int num = 5;
    private RatingBar rated_ratingbar;
    private ArrayList<Rated_Url> rated_img_list;
    private RatedEditGridviewAdapter adapter;

    private SharePreferenceUtil ShareUtil;
    private boolean IsConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratededit);
        ShareUtil = new SharePreferenceUtil(getApplicationContext());
        Goods goods = (Goods) getIntent().getSerializableExtra("goods");
        btn_util = new BtnUtil(getApplicationContext());
        initToolbar(goods.getGoodsName());
        initContent(goods);
        initEdit(goods);
    }

    private void initEdit(Goods goods) {
        goods_buy_btn = (IndeterminateProgressButton) this.findViewById(R.id.ratededit_confirm_id);
        goods_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.removeRated();
                IsConfirm = true;
                onMorphButton1Clicked(goods_buy_btn,"提交");
            }
        });
        btn_util.morphToSquare(goods_buy_btn, 0,"提交");
    }

    private int mMorphCounter1 = 1;

    private void onMorphButton1Clicked(IndeterminateProgressButton btnMorph, String text_str) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            btn_util.morphToSquare(btnMorph, 500,text_str);
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            btn_util.simulateProgress1(btnMorph,text_str);
        }
    }

    private void initContent(Goods goods) {
        MEditText = (MaterialEditText) this.findViewById(R.id.ratededit_edittext_id);
        MEditText.setText(ShareUtil.getRatedText());
        img_linear = (LinearLayout) this.findViewById(R.id.ratededit_img_linear_id);
        Button camera = (Button) this.findViewById(R.id.ratededit_camera);
        Button album = (Button) this.findViewById(R.id.ratededit_album);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_camera();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_album();
            }
        });
        rated_gridview = (GridView) findViewById(R.id.ratededit_gridview_id);
        rated_img_list = ShareUtil.getRatedImgList();
        if (rated_img_list.size()!=0) img_linear.setVisibility(View.VISIBLE);
        int gridview_width = LaunchActivity.windows_width;
        adapter = new RatedEditGridviewAdapter(RatedEditActivity.this, rated_img_list, gridview_width / num
                , new RatedEditGridviewAdapter.callbackListener() {
            @Override
            public void callback() {
                img_linear.setVisibility(View.GONE);
            }
        });
        rated_gridview.setAdapter(adapter);
        BaseUtil.setgridviewHeightBasedOnChildren(rated_gridview,num);
        rated_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录下点击的图片的位置和大小
                int[] screenLocation = new int[2];
                ImageView imageview = (ImageView) view.findViewById(R.id.rated_pic_id);
                imageview.getLocationOnScreen(screenLocation);
                Intent intent = new Intent(RatedEditActivity.this,PhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putSerializable("rated_url_list",rated_img_list);
                bundle.putInt("left", screenLocation[0]);
                bundle.putInt("top", screenLocation[1]);
                bundle.putInt("width", imageview.getWidth());
                bundle.putInt("height", imageview.getHeight());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_push_in,R.anim.anim_activity_push_out);
            }
        });
        rated_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setisShowDel(true);
                return true;
            }
        });

        rated_ratingbar = (RatingBar) this.findViewById(R.id.ratededit_ratingbar_id);
        rated_ratingbar.setRating(ShareUtil.getRatedLevel());
    }

    private void start_album() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, REQUEST_PICTURE);
    }

    private void start_camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        photoPath = BaseUtil.getSDPath() + "/Manicurists";
        File file = new File(photoPath);
        if (!file.exists()) {//检查图片存放的文件夹是否存在
            file.mkdir();//不存在的话 创建文件夹
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        photoName = sf.format(new Date(System.currentTimeMillis()))+ ".jpeg";
        File photo = new File(photoPath + "/" + photoName);
        Uri imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 这样就将文件的存储方式和uri指定到了Camera应用中
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void initToolbar(String goodsName) {
        Toolbar buy_toolebar = (Toolbar) this.findViewById(R.id.back_toolbar_id);
        ViewUtil.initAfterSetContentView(this, buy_toolebar);
        TextView title = (TextView) this.findViewById(R.id.toolbar_back_title_id);
        title.setText(goodsName);
        Button back = (Button) this.findViewById(R.id.toolbar_back_btn_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String sdStatus = Environment.getExternalStorageState();
        int width = LaunchActivity.windows_width / 6 - 10;
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Log.i("内存卡错误", "请检查您的内存卡");
                } else {
                    if (resultCode == -1) {//有拍照返回-1，没拍照返回0
                        String camera_img_uri = "file://" + photoPath + "/" + photoName;
                        Rated_Url rated_url = new Rated_Url();
                        rated_url.setUrl(camera_img_uri);
                        rated_img_list.add(rated_url);
                        adapter.notifyDataSetChanged();
                        BaseUtil.setgridviewHeightBasedOnChildren(rated_gridview, num);
                        //刷新相册
                        MediaScannerConnection.scanFile(this, new String[]{photoPath}, null, null);
                        if(img_linear.getVisibility() == View.GONE) img_linear.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case REQUEST_PICTURE:
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Log.i("内存卡错误", "请检查您的内存卡");
                } else {
                    //照片的原始资源地址
                    if (data != null) {
                        Uri originalUri = data.getData();
                        //使用ContentProvider通过URI获取原始图片
                        Rated_Url rated_url = new Rated_Url();
                        rated_url.setUrl(originalUri.toString());
                        rated_img_list.add(rated_url);
                        adapter.notifyDataSetChanged();
                        BaseUtil.setgridviewHeightBasedOnChildren(rated_gridview,num);
                        if(img_linear.getVisibility() == View.GONE) img_linear.setVisibility(View.VISIBLE);
                    }
                }
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(adapter.getisShowDel()){
                adapter.setisShowDel(false);
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保存用户的临时评论数据
        if (!IsConfirm)
            ShareUtil.saveRated(MEditText.getText().toString(),rated_img_list,rated_ratingbar.getRating());
    }
}
