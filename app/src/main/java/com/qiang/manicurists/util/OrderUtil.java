package com.qiang.manicurists.util;

import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.bean.Rated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/8 9:37
 * 修改备注：
 */
public class OrderUtil {
    public static int ORDER_UP = 0;
    public static int ORDER_DOWN = 1;

    /**
     * 按价格进行升降排序
     * @param old_list
     * @param type 0为升，1为降
     * @return
     */
    public static ArrayList<Goods> OrderForPrice(ArrayList<Goods> old_list, int type){
        if(type == ORDER_UP) {
            Collections.sort(old_list, new Comparator<Goods>() {
                @Override
                public int compare(Goods lhs, Goods rhs) {
                    return rhs.getGoodsPrice().compareTo(lhs.getGoodsPrice());
                }
            });
        }else{
            Collections.sort(old_list, new Comparator<Goods>() {
                @Override
                public int compare(Goods lhs, Goods rhs) {
                    return lhs.getGoodsPrice().compareTo(rhs.getGoodsPrice());
                }
            });
        }
        return  old_list;
    }

    /**
     * 筛选排序
     * @param old_list
     * @param left_price
     * @param right_price
     * @param date
     * @param time
     * @return
     */
    public static ArrayList<Goods> OrderForSiftings(ArrayList<Goods> old_list, int left_price,int right_price,String date,String time){
        ArrayList<Goods> new_list = new ArrayList<Goods>();
        for (int i =0;i<old_list.size();i++){
            Goods goods = old_list.get(i);
            int price = Integer.parseInt(goods.getGoodsDiscount().replace("蚊",""));
               if (price >= left_price && price <= right_price) new_list.add(goods);
            }
        return  new_list;
    }

    /**
     * 评价排序
     * @param old_list
     * @param level
     * @return
     */
    public static ArrayList<Rated> OrderForRated(ArrayList<Rated> old_list, String level){
        ArrayList<Rated> new_list = new ArrayList<Rated>();
        for (int i =0;i<old_list.size();i++){
            Rated rated = old_list.get(i);
            int price = Integer.parseInt(rated.getRatedLevel());
            if (price == Integer.parseInt(level)) new_list.add(rated);
        }
        return  new_list;
    }
}
