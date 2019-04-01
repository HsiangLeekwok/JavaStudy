package com.java.enjoy.spring.cap9.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description: 实现 BeanNameAware 和 ApplicationContextAware
 */
@Component
public class Light implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {

    private ApplicationContext applicationContext;
    private String beanName;

    public void setBeanName(String name) {
        System.out.println("传入的BeanName: " + name);
        this.beanName = name;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("传入的IOC容器：" + applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String result = resolver.resolveStringValue("Hello ${os.name}，计算#{3*8}");
        System.out.println("解析的字符串为：" + result);
    }
}
