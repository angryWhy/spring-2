package com.wzx.spring.mySpring.processor;
//参考spring原生容器
public interface BeanProcessor {
    default Object postProcessBeforeInitialization(Object bean,String beanName){
        return bean;
    }
    default Object postProcessAfterInitialization(Object bean,String beanName){
        return bean;
    }
}
