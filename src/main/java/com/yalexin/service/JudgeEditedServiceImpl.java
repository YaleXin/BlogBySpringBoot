package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class JudgeEditedServiceImpl implements JudgeEditedService{

    @Override
    public boolean isEdited(Blog oldBlog, Blog newBlog) {
        if (oldBlog.getTitle().equals(newBlog.getTitle()) &&
                oldBlog.getContent().equals(newBlog.getContent()) &&
                oldBlog.getFirstPicture().equals(newBlog.getFirstPicture()) &&
                oldBlog.getDescription().equals(newBlog.getDescription()) &&
                oldBlog.getFlag().equals(newBlog.getDescription()) &&
                oldBlog.getCreateTime().equals(newBlog.getCreateTime()) &&
                oldBlog.isRecommend() == newBlog.isRecommend() &&
                oldBlog.isShareStatement() == newBlog.isShareStatement() &&
                oldBlog.isAppreciation() == newBlog.isAppreciation() &&
                oldBlog.isCommentabled() == newBlog.isCommentabled()) {
            // 用户没有进行任何修改
            return false;
        }
        return true;
    }
}
