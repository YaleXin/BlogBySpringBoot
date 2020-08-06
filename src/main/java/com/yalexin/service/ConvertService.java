package com.yalexin.service;

import com.yalexin.entity.Blog;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
public interface ConvertService {
    /**
     * 将指定id的博客的content（MarkDown格式）转化为HTML
     * @param id
     * @return
     */
    Blog getAndConvert(Long id);
}
