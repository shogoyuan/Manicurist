package com.qiang.manicurists.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：评价实体类
 * 创建人：Qiang
 * 创建时间：2016/6/30 10:13
 * 修改备注：
 */
public class Rated  implements Serializable {
    private static final long serialVersionUID = -1123581321345590L;
    //评价头像
    private String ratedIcon;
    //评价号码
    private String ratedNum;
    //评价等级
    private String ratedLevel;
    //评价内容
    private String ratedContent;
    //评价附带的图片
    private ArrayList<Rated_Url> ratedPicUrl;
    //评价时间
    private String ratedDate;

    public void setRatedIcon(String RatedIcon){
        this.ratedIcon = RatedIcon;
    }
    public String getRatedIcon(){
        return  this.ratedIcon;
    }

    public void setRatedNum(String RatedNum){
        this.ratedNum = RatedNum;
    }
    public String getRatedNum(){
        return  this.ratedNum;
    }

    public void setRatedLevel(String RatedLevel){
        this.ratedLevel = RatedLevel;
    }
    public String getRatedLevel(){
        return  this.ratedLevel;
    }

    public void setRatedContent(String RatedContent){
        this.ratedContent = RatedContent;
    }
    public String getRatedContent(){
        return  this.ratedContent;
    }

    public void setRatedPicUrl(ArrayList<Rated_Url> ratedPicUrl){
        this.ratedPicUrl = ratedPicUrl;
    }
    public ArrayList<Rated_Url> getRatedPicUrl(){
        if (ratedPicUrl == null) ratedPicUrl = new ArrayList<>();
        return  this.ratedPicUrl;
    }

    public void setRatedDate(String RatedDate){
        this.ratedDate = RatedDate;
    }
    public String getRatedDate(){
        return  this.ratedDate;
    }
}
