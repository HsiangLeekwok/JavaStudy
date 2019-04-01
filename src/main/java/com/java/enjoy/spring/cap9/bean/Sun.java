package com.java.enjoy.spring.cap9.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */
@Component
public class Sun {

    private Moon moon;

    @Autowired
    public Sun(Moon moon) {
        this.moon = moon;
    }

    public Moon getMoon() {
        return moon;
    }

    public void setMoon(Moon moon) {
        this.moon = moon;
    }

    @Override
    public String toString() {
        return "Sun{" +
                "moon=" + moon +
                '}';
    }
}
