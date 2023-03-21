package com.wzx.spring.mySpring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Target(ElementType.TYPE)，指定ComponentScan注解可以修饰Type程序元素
//@Retention(RetentionPolicy.RUNTIME)，指定ComponentScan作用范围
//String value() default ""，可以传入一个value属性
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {
    String value() default "";
}
