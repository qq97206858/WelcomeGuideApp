package com.example.Annot.AnnotModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmployeeSex {
    enum Sex{Man,Woman}//
    Sex employeeSex()  default Sex.Man;
}
