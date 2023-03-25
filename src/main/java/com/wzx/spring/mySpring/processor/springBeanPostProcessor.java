package com.wzx.spring.mySpring.processor;

import com.wzx.spring.mySpring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component()
public class springBeanPostProcessor implements BeanProcessor{
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("后置处理器 before");
        return BeanProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("后置处理器 after");
        if("Aspect".equals(beanName)){
            Object beanProxy = Proxy.newProxyInstance(BeanProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
            return beanProxy;
        }
        return BeanProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
