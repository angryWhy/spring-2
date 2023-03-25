package com.wzx.spring.mySpring.processor;
//模拟bean-process机制
public interface InitialzingBean {
    //afterPropertiesSet是bean在执行setter方法后，在spring容器中被调用
    void afterPropertiesSet() throws Exception;
}
