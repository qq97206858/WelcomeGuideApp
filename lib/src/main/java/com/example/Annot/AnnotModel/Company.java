package com.example.Annot.AnnotModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Company {
    /**
     * 公司注册编号
     * @return
     */
    public int id() default -1;

    /**
     * 公司名称
     * @return
     */
    public String name() default "";

    /**
     * 公司地址
     * @return
     */
    public String address() default "";
}
