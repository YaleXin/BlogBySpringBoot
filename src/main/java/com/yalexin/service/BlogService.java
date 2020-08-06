package com.yalexin.service;

import com.yalexin.entity.Blog;
import com.yalexin.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
public interface BlogService {
    Blog getBlog(Long id);

//    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable, Long tagId);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String query, Pageable pageable);

    Map<String,List<Blog>> archivesBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    List<Blog> listRecommendBlogTop(Integer size);

    void deleteBlog(Long id);

    Blog getBlogByTitle(String title);

//    boolean isEdited(Long oldBlogId, Blog newBlog);

    Long countBlog();

}
