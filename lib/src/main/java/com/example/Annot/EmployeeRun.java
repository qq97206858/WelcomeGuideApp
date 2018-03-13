package com.example.Annot;

import com.example.Annot.AnnotProcessor.EmployeeInfoUtil;
import com.example.Annot.AnnotUseage.EmployeeInfo;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by fqzhang on 2017/8/19.
 */

public class EmployeeRun {
    public static void main(String[] args) {
        Map fruitInfo = EmployeeInfoUtil.getEmployeeInfo(EmployeeInfo.class);
        System.out.println(fruitInfo);
    }
}
