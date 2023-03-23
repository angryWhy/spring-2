package com.wzx.spring.mySpring;
@Component(value = "Dao")
public class Dao {
    public void say(){
        System.out.println("say被调用");
    }
}
