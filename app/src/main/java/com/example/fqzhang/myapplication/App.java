package com.example.fqzhang.myapplication;

import android.app.Application;

import com.example.fqzhang.myapplication.Util.FoundationContextHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fqzhang on 2017/8/22.
 */

public class App extends Application {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
       /* JSONArray j;j.put*/
  /*      try {
            System.out.println(jsonObject.put("zfq","zz").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       /* try{
            jsonObject.put("name", "zfq");
            JSONObject jso = new JSONObject();
            for (int i=0;i<3;i++){
                jso.put("q"+i,i);
            }
            jsonObject.put("content",jso);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject.toString());*/
    }
    @Override
    public void onCreate() {
        super.onCreate();
        FoundationContextHolder.setContext(this);
    }
}
