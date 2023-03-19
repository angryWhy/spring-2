package com.wzx.spring.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;
    public void m1(){
        userDao.hi();
    }
    //需要指定init方法，bean的初始化方法
    // @PostConstruct,指定初始化方法
    @PostConstruct
    public void init(){
        System.out.println("userService-init");
    }
}
