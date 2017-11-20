package com.keymanager.util;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * @author LiuJie
 * @create 2017-11-20 9:26
 * @desc
 */
public class PageCustomize<T> extends Page<T>{
    private  int pages;
    public int getPages(int size, int total) {
        if(size == 0) {
            return 0;
        } else {
            this.pages = total / size;
            if(total % size != 0) {
                ++this.pages;
            }
            return this.pages;
        }
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
