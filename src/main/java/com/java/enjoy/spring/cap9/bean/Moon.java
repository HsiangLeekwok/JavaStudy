package com.java.enjoy.spring.cap9.bean;

import org.springframework.stereotype.Component;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */
@Component
public class Moon {

    public Moon(){
        System.out.println("Moon constructor........");
    }

    public void init(){
        System.out.println("Moon....init......");
    }


    public void destroy(){
        System.out.println("Moon....destroy......");
    }
}
