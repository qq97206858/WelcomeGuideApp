package com.example.fqzhang.myapplication.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;

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
}
