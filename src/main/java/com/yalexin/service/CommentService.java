package com.yalexin.service;

import com.yalexin.entity.Comment;
import com.yalexin.vo.CommentQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    Page<Comment> listComment(Pageable pageable);

    Comment getComment(Long id);

    Comment updateComment(Long id,Comment comment);

    Page<Comment> listComment(Pageable pageable, CommentQuery commentQuery);

    public void  deleteComment(Long id);
}
