package com.yalexin.web;

import com.yalexin.entity.Blog;
import com.yalexin.entity.Tag;
import com.yalexin.service.BlogServiceImpl;
import com.yalexin.service.TagServiceImpl;
import com.yalexin.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/tags")
public class TagShowController {
    @Autowired
    private TagServiceImpl TagService;

    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping("/{id}")
    public String Tags(@PageableDefault(size = 6, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable("id") Long id, Model model) {
        List<Tag> Tags = TagService.listTAg(10000);
        if (id == -1) {
            id = Tags.get(0).getId();
        }
        model.addAttribute("tags", Tags);
        Page<Blog> blogs = blogService.listBlog(pageable, id);
        model.addAttribute("page", blogs);
        model.addAttribute("activeId", id);
        return "tags";
    }
}
