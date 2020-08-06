package com.yalexin.service;

import com.yalexin.entity.Comment;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */

/**
 * 邮件发送服务
 */
public interface SendEmailService {
    /**
     *
     * @param parentComment 父级评论
     * @param replyComment  子级评论
     * @param is2Parent      是否是发送到父级评论
     */
    void send(Comment parentComment, Comment replyComment,boolean is2Parent);
}
