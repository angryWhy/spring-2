package com.wzx.spring.aop;

import org.springframework.stereotype.Component;

@Component
public class Dog implements SmartAnimal{
    @Override
    public void say(int n) {
        System.out.println("say!");
    }
}
