package com.example.Annot.AnnotModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE.TYPE)
public @interface Table {
    /**
     * 数据表名注解，默认值为类名称
     * @return
     */
    public String tableName() default "className";
}
