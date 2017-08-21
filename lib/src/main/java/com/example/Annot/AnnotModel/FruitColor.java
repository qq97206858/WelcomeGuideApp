package com.example.Annot.AnnotModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitColor {
    public enum Color{BLUE,RED,GREEN};

    /**
     * 颜色属性
     * @return
     */
    Color fruitColor()  default Color.GREEN;
}
