package com.example.Annot.AnnotProcessor;

import com.example.Annot.AnnotModel.Company;
import com.example.Annot.AnnotModel.EmployeeName;
import com.example.Annot.AnnotModel.EmployeeSex;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fqzhang on 2017/8/19.
 */

public class EmployeeInfoUtil {
    public static Map getEmployeeInfo(Class<?> clazz){
        HashMap<String ,String> info = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            if (field.isAnnotationPresent(EmployeeName.class)){
                EmployeeName employeeName = field.getAnnotation(EmployeeName.class);
                info.put("employeeName",employeeName.value());
            }
            if (field.isAnnotationPresent(EmployeeSex.class)) {
                EmployeeSex employeeSex = field.getAnnotation(EmployeeSex.class);
                info.put("employeeSex",employeeSex.employeeSex().toString());
            }

            if (field.isAnnotationPresent(Company.class)) {
                Company company = field.getAnnotation(Company.class);
                info.put("company",company.id()+":"+company.name()+":"+company.address());
            }
        }
        return info;
    }
}
