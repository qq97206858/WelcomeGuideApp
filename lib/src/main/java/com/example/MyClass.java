package com.example;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;

public class MyClass implements Cloneable{
    public static void main(String[] args) {
        String[] strs = {"我们都有一个个家就","夏威夷*火奴奴鲁鲁了"};
         Object obj = new Object(){
            {
                System.out.println("代码块！！！");
            }
             @Override
             public String toString() {
                 return super.toString();
             }
         };
    }
    private static void dealOverLength4CityName (String[] cityNames) {
        String departCity = cityNames[0];
        String arriveCity =cityNames[1];
            if (departCity.length()+arriveCity.length() > 12) {
                if (departCity.length() > 6){
                    cityNames[0] = departCity.replace(departCity.charAt(6)+"","\n"+departCity.charAt(6));
                }
                if (arriveCity.length() > 6) {
                    cityNames[1] = arriveCity.replace(arriveCity.charAt(6)+"","\n"+arriveCity.charAt(6));
                }
            }

    }
    public String Var;
    public MyClass () {
        Var = "你好";
        System.out.println("constructor run...");
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new MyClass();
    }
 /*    public void method () {
        MObservable mObservable = new MObservable();
        mObservable.addObserver(new MObserver());
        mObservable.notifyObservers();
    }
   class MObservable extends  Observable {
        @Override
        protected synchronized void setChanged() {
            super.setChanged();super.changed = true;
        }
    }
    class MObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {

        }
    }*/
    enum Season{
     Summer,
     Winter,
     Spring,
     Autumn
 }
}
