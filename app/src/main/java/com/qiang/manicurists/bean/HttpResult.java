package com.qiang.manicurists.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class HttpResult<T> {
    //对应服务器返回的字节参数名字
    private int code;
    private String msg;
    private ArrayList<T> info;

    public int getCode(){
        return code;
    }
    public void setCode(int code){
        this.code = code;
    }

    public String getMsg(){
        return msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public ArrayList<T> getInfo(){
        return  info;
    }

    public void setInfo(ArrayList<T> info){
        this.info = info;
    }
}
