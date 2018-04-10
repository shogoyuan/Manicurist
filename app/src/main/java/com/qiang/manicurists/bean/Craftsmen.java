package com.qiang.manicurists.bean;

import java.io.Serializable;

/**
 * 项目名称：Manicurists
 * 类描述：手艺人实体类
 * 创建人：Qiang
 * 创建时间：2016/6/30 10:14
 * 修改备注：
 */
public class Craftsmen  implements Serializable {
    private static final long serialVersionUID = -1123581321345591L;
    //手艺人头像
    private String craftsmenIcon;
    //手艺人名字
    private String craftsmenName;
    //手艺人职称
    private String craftsmenPosition;
    //手艺人等级
    private String craftsmenLevel;

    public void setCraftsmenIcon(String CraftsmenIcon){
        this.craftsmenIcon = CraftsmenIcon;
    }

    public String getCraftsmenIcon(){
        return this.craftsmenIcon;
    }

    public void setCraftsmenName(String CraftsmenName){
        this.craftsmenName = CraftsmenName;
    }

    public String getCraftsmenName(){
        return this.craftsmenName;
    }

    public void setCraftsmenPosition(String CraftsmenPosition){
        this.craftsmenPosition = CraftsmenPosition;
    }

    public String getCraftsmenPosition(){
        return this.craftsmenPosition;
    }

    public void setCraftsmenLevel(String CraftsmenLevel){
        this.craftsmenLevel = CraftsmenLevel;
    }

    public String getCraftsmenLevel(){
        return this.craftsmenLevel;
    }
}
