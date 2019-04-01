package com.java.enjoy.spring.cap9.controller;

import com.java.enjoy.spring.cap9.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */
@Controller
public class TestController {
    @Autowired
    private TestService testService;
}
