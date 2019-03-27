package com.java.enjoy.thread.forkjoin;

import java.util.Random;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/22 22:36
 * Version: v1.0
 * Description: 数组工具
 */
public class ArrayTool {

    static final int ARRAY_SIZE = 100000000;

    /**
     * 初始化数组值
     */
    static int[] makeArray() {
        Random random = new Random();
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(ARRAY_SIZE * 2);
        }
        return array;
    }
}
