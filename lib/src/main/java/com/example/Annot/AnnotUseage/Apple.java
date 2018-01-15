package com.example.Annot.AnnotUseage;

import com.example.Annot.AnnotModel.FruitColor;
import com.example.Annot.AnnotModel.FruitName;
import com.example.Annot.AnnotModel.FruitProvider;

/**
 * Created by fqzhang on 2017/8/19.
 */

public class Apple {
    @FruitName("Apple")
    private String appleName;
    @FruitColor(fruitColor = FruitColor.Color.GREEN)
    private String appleColor;
    @FruitProvider(id = 1,name = "红富士集团",address = "河南开封")
    private String appleProvider;
    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }
    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }

    public String getAppleProvider() {
        return appleProvider;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }
    public String getAppleName() {
        return appleName;
    }

    public void displayName(){
        System.out.println("水果的名字是：苹果");
    }
}
