package com.qiang.manicurists.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiang.manicurists.bean.Craftsmen;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.bean.HttpResult;
import com.qiang.manicurists.bean.Rated;
import com.qiang.manicurists.util.BaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/18.
 */
public class HttpTest {
    /*测试的服务器网址*/
    public static final String Goods_url = "http://192.168.2.7:81/phalapi/public/manicurists/?service=Goods.getAllGoods";
    public static final String Rated_url = "http://192.168.1.109:81/phalapi/public/manicurists/?service=Rated.getBaseInfo&rated_id=";
    public static final String Craftsmen_url = "http://192.168.1.109:81/phalapi/public/manicurists/?service=Craftsmen.GetBaseInfo&craftsmen_id=";
    public static final String RatedUrl_url = "http://192.168.1.109:81/phalapi/public/manicurists/?service=Rated.getRatedUrl&rated_id=";

    private OKHttpUtils mokhttputils;
    public HttpTest(OKHttpUtils okhttputils){
        mokhttputils = okhttputils;
    }

    public void getGoodsData(final HttpListener httplistener){
        Request request = new Request.Builder().url(Goods_url)
//                .addHeader("User-Agent","android")
//                .header("Content-Type","text/html; charset=utf-8")
                .build();
        mokhttputils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httplistener.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //这里还不是主线程
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(response.body().string());
                    JSONObject str = json.getJSONObject("data");
                    HttpResult<Goods> goods = gson.fromJson(str.toString(),new TypeToken<HttpResult<Goods>>(){}.getType());
                    httplistener.onResponse(call,goods,0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGoodsRated(final int goods_id,final HttpListener httplistener){
        Request request = new Request.Builder().url(Rated_url+goods_id)
                .build();
        mokhttputils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httplistener.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //这里还不是主线程
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(response.body().string());
                    JSONObject str = json.getJSONObject("data");
                    HttpResult<Rated> rated = gson.fromJson(str.toString(),new TypeToken<HttpResult<Rated>>(){}.getType());
                    httplistener.onResponse(call,rated,goods_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGoodsCraftsmen(final int goods_id,final HttpListener httplistener){
        Request request = new Request.Builder().url(Craftsmen_url+goods_id)
                .build();
        mokhttputils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httplistener.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //这里还不是主线程
                    Gson gson = new Gson();
                    JSONObject json = new JSONObject(response.body().string());
                    JSONObject str = json.getJSONObject("data");
                    HttpResult<Craftsmen> goods = gson.fromJson(str.toString(),new TypeToken<HttpResult<Craftsmen>>(){}.getType());
                    httplistener.onResponse(call,goods,goods_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGoodsRated_pic(final int goods_id,final int rated_id,final HttpListener2 httplistener){
        Request request = new Request.Builder().url(RatedUrl_url+rated_id)
                .build();
        mokhttputils.request(request, CacheType.ONLY_NETWORK, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httplistener.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //这里还不是主线程
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray array = json.getJSONObject("data").getJSONArray("info");
                    ArrayList<String> rated_url = new ArrayList<String>();
                    for (int i = 0;i<array.length();i++) {
                        rated_url.add(array.getJSONObject(i).getString("url"));
                    }
                    httplistener.onResponse(call,rated_url,goods_id,rated_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void getData(@CacheType int cacheType){
//        HashMap<String,String> hash = new HashMap<>();
//        StringBuffer url_str = new StringBuffer();
//        url_str.append(url);
//        String url1 = okHttpUtils.getGetUrl(url_str,hash);
//        BaseUtil.ShowLog("123","url:"+url1);
//        RequestBody body = okHttpUtils.createRequestBody(hash);
//        Request request = new Request.Builder().url(url)
////                .addHeader("User-Agent","android")
////                .header("Content-Type","text/html; charset=utf-8")
//                .build();
//        okHttpUtils.request(request, cacheType, jsonCallback);
//    }
//    private JsonCallback<DateModule> jsonCallback = new JsonCallback<DateModule>() {
//        @Override
//        public void onFailure(Call call, Exception e) {
//            BaseUtil.ShowLog("123","fail  "+e.toString()+"12");
//        }
//
//        @Override
//        public void onResponse(Call call, final DateModule object) throws IOException {
//            if(object!=null){
//                BaseUtil.ShowLog("123","success  "+object.getResult().getret()+"  12");
//            }
//        }
//    };

    public interface HttpListener{
        void onFailure(Call call, IOException e);
        void onResponse(Call call, Object object,int id);
    }
    public interface HttpListener2{
        void onFailure(Call call, IOException e);
        void onResponse(Call call, Object object,int goods_id,int rated_id);
    }
}
