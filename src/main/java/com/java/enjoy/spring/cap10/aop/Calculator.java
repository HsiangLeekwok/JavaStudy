package com.java.enjoy.spring.cap10.aop;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */
public class Calculator {
    /**
     * 业务逻辑方法
     */
    public int div(int i, int j) {
        System.out.println("method self execute begin.......");
        int ret = i / j;
        System.out.println("method self execute end.........");
        return ret;
    }
}
