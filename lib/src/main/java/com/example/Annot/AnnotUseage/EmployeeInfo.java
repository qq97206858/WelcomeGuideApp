package com.example.Annot.AnnotUseage;

import com.example.Annot.AnnotModel.Company;
import com.example.Annot.AnnotModel.EmployeeName;
import com.example.Annot.AnnotModel.EmployeeSex;

/**
 * Created by fqzhang on 2017/8/19.
 */

public class EmployeeInfo {
    @EmployeeName("zfq")
    private String employeeName;
    @EmployeeSex(employeeSex = EmployeeSex.Sex.Woman)
    private String employeeSex;
    @Company(id = 1,name = "HYR集团",address = "河南开封")
    private String company;

    public void setCompany(String company) {
        this.company = company;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeSex(String employeeSex) {
        this.employeeSex = employeeSex;
    }

    public String getCompany() {
        return company;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeSex() {
        return employeeSex;
    }

    public void displayName(){
        System.out.println("员工的名字是："+employeeName);
    }
}
