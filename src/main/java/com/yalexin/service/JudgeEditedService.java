package com.yalexin.service;

import com.yalexin.entity.Blog;
import org.springframework.stereotype.Service;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */

public interface JudgeEditedService {
    /**
     * 判断用户是否修改了博客
     * @param oldBlog 数据库中的博客
     * @param newBlog 表单提交过来的博客
     * @return
     */
    boolean isEdited(Blog oldBlog, Blog newBlog);
}
