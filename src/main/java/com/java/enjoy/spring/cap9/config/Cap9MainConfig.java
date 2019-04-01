package com.java.enjoy.spring.cap9.config;

import com.java.enjoy.spring.cap9.bean.Light;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */
@Configuration
@ComponentScan({"com.java.enjoy.spring.cap9"})
public class Cap9MainConfig {

//    @Bean
//    public Light light() {
//        return new Light();
//    }
}
