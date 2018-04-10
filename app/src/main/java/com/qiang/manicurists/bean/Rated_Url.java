package com.qiang.manicurists.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Rated_Url implements Serializable {
    private static final long serialVersionUID = -1123581321345545L;

    private String url;

    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return url;
    }
}
