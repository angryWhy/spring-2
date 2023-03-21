package com.wzx.spring.mySpring;

import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class IOC {
    private Class configClass;
    //存放的是通过反射创建的对象（基于注解）
    private ConcurrentHashMap<String,Object> ioc = new ConcurrentHashMap<>();
    public IOC(Class config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.configClass = config;

        //获取扫描的包，1，现货区SpringConfig的注解,拿到配置类上的注解
        ComponentScan ComponentScan = (ComponentScan)this.configClass.getDeclaredAnnotation(ComponentScan.class);
        //2.通过componentScan的value来获取=>扫描的包
        String path = ComponentScan.value();
        path = path.replace("/",".");
        //先得到类的加载器,获取扫描包的url

        ClassLoader classLoader = IOC.class.getClassLoader();

        URL resource = classLoader.getResource("src/com");
        File file = new File(resource.getFile());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                String absolutePath = f.getAbsolutePath();
                if(!absolutePath.endsWith(".class")) continue;
                //先获取到类名
                String className = absolutePath.substring(absolutePath.lastIndexOf("\\") + 1, absolutePath.indexOf(".class"));
                String name =  path.replace(".","/")+"." + className;

                Class<?> aClass = classLoader.loadClass(name);
                if(aClass.isAnnotationPresent(Component.class)||aClass.isAnnotationPresent(Controller.class)){
                    Class<?> aClass1 = Class.forName(name);
                    Object instance = aClass1.newInstance();
                    ioc.put(name,instance);
                }else{
                    System.out.println("没有添加注解");
                }
            }
        }
    }
    public Object getBean(String name){
        return null;
    }
}
