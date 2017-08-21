package com.example.Annot.AnnotModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.ANNOTATION_TYPE.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitName {
    String value () default "";
}
