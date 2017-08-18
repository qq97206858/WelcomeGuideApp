package com.example.fqzhang.myapplication.Util;

import android.app.Application;
import android.content.Context;

/**
 * Created by fqzhang on 2017/8/18.
 */

public class FoundationContextHolder {
    public static Context context;
    private static Application mainApplication;

    public FoundationContextHolder() {
    }

    public static Application getApplication() {
        return mainApplication;
    }

    public static void setApplication(Application application) {
        mainApplication = application;
    }

    public static void setContext(Context context) {
        context = context;
    }

    public static Context getContext() {
        return context;
    }
}
