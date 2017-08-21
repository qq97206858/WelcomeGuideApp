package com.example.Annot;

import com.example.Annot.AnnotProcessor.FruitInfoUtil;
import com.example.Annot.AnnotUseage.Apple;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by fqzhang on 2017/8/19.
 */

public class FruitRun {
    public static void main(String[] args) {
        Map fruitInfo = FruitInfoUtil.getFruitInfo(Apple.class);
        System.out.println(fruitInfo);
    }
}
