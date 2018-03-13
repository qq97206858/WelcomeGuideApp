package com.example.fqzhang.myapplication.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fqzhang on 2017/11/30.
 */

public class ZUtil extends StringUtil{
    /**
     * 查看一个字符串是否可以转换为数字
     * @param str 字符串
     * @return true 可以; false 不可以
     */
    public static boolean isStr2Num(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public static Drawable getGradientBG (int colorID) {
        Context context = null;
        Drawable[] layers = new Drawable[2];
        int color;
        if (context == null) {
            color = 0xFF08A8E4;
        } else {
            color = context.getResources().getColor(colorID);
        }
        layers[0] = new ColorDrawable(color);//背景色
        int colors[] = { 0x00ffffff , 0xFFEEEEF3, 0xFFEEEEF3 };//分别为开始颜色，中间夜色，结束颜色
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        layers[1] = gradientDrawable;//前景色
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        return layerDrawable;
    }
    /**
     * 判断一个日期是不是有效的八位日期
     * @param str 八位时间str
     * @return true为有效时间
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年两位月份两位日期，注意yyyyMMdd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如20070229会被接受，并转换成20070301
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        } catch (NullPointerException e){
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
