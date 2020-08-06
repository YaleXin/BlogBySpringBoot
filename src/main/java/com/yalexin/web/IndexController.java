package com.yalexin.web;

import com.yalexin.NotFoundException;
import com.yalexin.entity.Blog;
import com.yalexin.entity.Comment;
import com.yalexin.service.*;
import com.yalexin.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
public class IndexController {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TypeServiceImpl typeService;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private ConvertServiceImpl convertService;

    @Autowired
    private CommentServiceImpl commentService;

    @Value("${blog_host}")
    private String blog_host;

    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listType(6));
        model.addAttribute("tags", tagService.listTAg(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable("id") Long id, Model model) {


        Blog blog = convertService.getAndConvert(id);

        model.addAttribute("blog", blog);

        List<Comment> comments = commentService.listCommentByBlogId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("blog_host",blog_host);
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
