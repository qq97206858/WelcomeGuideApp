package com.example;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MyClass implements Cloneable{
/*    public static  void main (String[] args) {
        try {
            Object clone = new MyClass().clone();
            System.out.println(((MyClass)clone).Var);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }*/

    public String Var;
    public MyClass () {
        Var = "ÄãºÃ";
        System.out.println("constructor run...");
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new MyClass();
    }
    public void method () {
        MObservable mObservable = new MObservable();
        mObservable.addObserver(new MObserver());
        mObservable.notifyObservers();mObservable.hasChanged()
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
    }
}
