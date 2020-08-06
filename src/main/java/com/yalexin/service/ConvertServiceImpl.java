package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.dao.BlogRepository;
import com.yalexin.entity.Blog;
import com.yalexin.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class ConvertServiceImpl implements ConvertService {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogService.getBlog(id);
        if (blog == null) {
            throw new NotFoundException("不存在该博客文章");
        }
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog, blog1);
        String content = blog1.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return blog1;
    }
}
