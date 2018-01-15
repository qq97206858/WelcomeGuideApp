package com.example.fqzhang.myapplication.model;

import com.example.Seriable;

/**
 * Created by fqzhang on 2018/1/15.
 */

public class User {
    @Seriable
    private String username;
    @Seriable
    private String password;

    private String three;
    private String four;
}
