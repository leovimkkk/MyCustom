package com.xwjr.utilcode.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/5/3.
 */

public class DecimalUtil {

    public static String keep2nb(double d) {

        DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00");
        return df.format(d);
    }

    public static String keep1nb(double d) {

        DecimalFormat df = new DecimalFormat("###,###,###,###,##0.0");
        return df.format(d);
    }

    public static String keep5nb(double d) {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(d);
    }

    public static String keep0nb(double d){
        DecimalFormat df = new DecimalFormat("0");
        return df.format(d);
    }
    public static String keep0nbSeparate(double d){
        DecimalFormat df = new DecimalFormat("###,###,###,###,##0");
        return df.format(d);
    }

    public static String subZeroAndDot(Double d){
        String s = String.valueOf(d);
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        DecimalFormat df;
        if(s.contains(".")){
            df = new DecimalFormat("###,###,###,###,##0.00");
        }else{
            df = new DecimalFormat("###,###,###,###,##0");
        }

        return df.format(Double.valueOf(s));
    }
}
