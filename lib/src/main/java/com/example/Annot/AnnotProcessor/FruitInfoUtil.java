package com.example.Annot.AnnotProcessor;

import com.example.Annot.AnnotModel.FruitColor;
import com.example.Annot.AnnotModel.FruitName;
import com.example.Annot.AnnotModel.FruitProvider;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fqzhang on 2017/8/19.
 */

public class FruitInfoUtil {

    public static Map getFruitInfo(Class<?> clazz){
        HashMap<String ,String> info = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            if (field.isAnnotationPresent(FruitName.class)){
                FruitName fruitName = field.getAnnotation(FruitName.class);
                info.put("appleName",fruitName.value());
            }
            if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                info.put("fruitColor",fruitColor.fruitColor().toString());
            }

            if (field.isAnnotationPresent(FruitProvider.class)) {
                FruitProvider fruitProvider = field.getAnnotation(FruitProvider.class);
                info.put("fruitProviderInfo",fruitProvider.id()+":"+fruitProvider.name()+":"+fruitProvider.address());
            }
        }
        return info;
    }

}
