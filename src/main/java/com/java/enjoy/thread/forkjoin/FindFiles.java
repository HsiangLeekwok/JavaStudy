package com.java.enjoy.thread.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/03/23 12:33
 * Version: v1.0
 * Description: 扫描指定目录以及其下所有子目录并且找到指定类型文件
 */
public class FindFiles extends RecursiveAction {

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        FindFiles task = new FindFiles(new File("E:/"));
        pool.execute(task);
        System.out.println("Thread is running....");
        task.join();
        System.out.println("Thread is over.");
    }

    private File path;

    private FindFiles(File path) {
        this.path = path;
    }

    @Override
    protected void compute() {
        File[] files = path.listFiles();
        if (null != files) {
            List<FindFiles> tasks = new ArrayList<>();
            for (File file : files) {
                if (file.isDirectory()) {
                    tasks.add(new FindFiles(file));
                } else {
                    if (file.getAbsolutePath().endsWith(".cs")) {
                        System.out.println("找到C#文件：" + file.getAbsolutePath());
                    }
                }
            }
            if (!tasks.isEmpty()) {
                invokeAll(tasks);
            }
        }
    }
}
