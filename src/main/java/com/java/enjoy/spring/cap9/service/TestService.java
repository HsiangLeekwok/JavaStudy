package com.java.enjoy.spring.cap9.service;

import com.java.enjoy.spring.cap9.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */
@Service
public class TestService {

    @Autowired
    TestDao testDao;
}
