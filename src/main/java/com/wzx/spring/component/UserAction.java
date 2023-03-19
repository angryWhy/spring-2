package com.wzx.spring.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//这是一个control
@Component
//配置为多例，默认是单例，以多实例形式，返回UserAction，每次返回一个新的
@Scope(value = "prototype")
public class UserAction {
}
