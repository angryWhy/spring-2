package com.wzx.spring.mySpring;

import com.wzx.spring.mySpring.processor.BeanProcessor;
import com.wzx.spring.mySpring.processor.InitialzingBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class IOC {
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private List<BeanProcessor> beanProcessorList = new ArrayList<>();
    private Class configClass;
    //存放的是通过反射创建的对象（基于注解）
    public IOC(Class config) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        //完成扫描包
        beanDefinitionScan(config);
        //初始化单例池
        //遍历所有的beandefinition
        Enumeration<String> keys = beanDefinitionMap.keys();
        //遍历集合
        while (keys.hasMoreElements()) {
            String beanName = keys.nextElement();
            //拿到beanName
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equalsIgnoreCase(beanDefinition.getScope())) {
                Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }
    public void beanDefinitionScan(Class config) throws ClassNotFoundException {
        this.configClass = config;
        //获取扫描的包，1，现货区SpringConfig的注解,拿到配置类上的注解
        ComponentScan ComponentScan = (ComponentScan) this.configClass.getDeclaredAnnotation(ComponentScan.class);
        //2.通过componentScan的value来获取=>扫描的包
        String path = ComponentScan.value();
        path = path.replace("/", ".");
        //先得到类的加载器,获取扫描包的url

        ClassLoader classLoader = IOC.class.getClassLoader();

        URL resource = classLoader.getResource("src/com");
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String absolutePath = f.getAbsolutePath();
                if (!absolutePath.endsWith(".class")) continue;
                //先获取到类名
                String className = absolutePath.substring(absolutePath.lastIndexOf("\\") + 1, absolutePath.indexOf(".class"));
                String name = path.replace(".", "/") + "." + className;

                Class<?> aClass = classLoader.loadClass(name);
                if (aClass.isAnnotationPresent(Component.class) || aClass.isAnnotationPresent(Controller.class)) {
                    BeanDefinition beanDefinition = new BeanDefinition();
                    //1.解决名字称题
                    Component declaredAnnotation = aClass.getDeclaredAnnotation(Component.class);
                    String beanName = declaredAnnotation.value();
                    if ("".equals(beanName)) {
                        //如果没有指定value名字，
                        beanName = StringUtils.uncapitalize(className);
                    }
                    beanDefinition.setClazz(aClass);
                    if (aClass.isAnnotationPresent(Scope.class)) {
                        Scope declaredAnnotation1 = aClass.getDeclaredAnnotation(Scope.class);
                        beanDefinition.setScope(declaredAnnotation1.value());
                    } else {
                        beanDefinition.setScope("singleton");
                    }
                    beanDefinitionMap.put(name, beanDefinition);
                } else {
                    System.out.println("没有添加注解");
                }
            }
        }
    }

    public Object createBean(String beanName,BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for(Field key :clazz.getDeclaredFields()){
                if(key.isAnnotationPresent(Autowired.class)){
                    String name = key.getName();
                    //拿到bean对象,进行依赖注入
                    Object bean = getBean(name);
                    //属性是私有的需要爆破
                    key.setAccessible(true);
                    //设置（对象，值）
                    key.set(instance,bean);
                }
            }
            if(BeanProcessor.class.isAssignableFrom(clazz)){
                BeanProcessor o = (BeanProcessor)clazz.newInstance();
                beanProcessorList.add(o);
            }

            //beanpostprocessor的before方法
            for(BeanProcessor bean : beanProcessorList){
                Object o = bean.postProcessBeforeInitialization(instance, beanName);
            }

            if(instance instanceof InitialzingBean){
                ((InitialzingBean)instance).afterPropertiesSet();
            }
            //beanpostprocessor的after方法
            for(BeanProcessor bean : beanProcessorList){
                Object o = bean.postProcessBeforeInitialization(instance, "...");
            }
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String name) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (beanDefinitionMap.get(name) != null) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(name);
            if ("singleton".equalsIgnoreCase(beanDefinition.getScope())) {
                return singletonObjects.get(name);
            } else if ("prototype".equalsIgnoreCase(beanDefinition.getScope())) {
                Class clazz = beanDefinition.getClazz();
                Object o = clazz.getDeclaredConstructor().newInstance();
                return o;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
