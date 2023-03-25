package com.wzx.spring.mySpring;

import com.wzx.spring.mySpring.processor.InitialzingBean;

@Component( value = "myService")
//如果指定了value，则value注入，如果未指定，按类名首字母小写来注入
public class Service implements InitialzingBean {
    @Autowired
    private Dao dao;

    @Override
    public void afterPropertiesSet() throws Exception {
//        在这里加入初始化代码或者业务
    }
}
