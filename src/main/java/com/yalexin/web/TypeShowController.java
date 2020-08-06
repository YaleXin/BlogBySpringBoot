package com.yalexin.web;

import com.yalexin.entity.Blog;
import com.yalexin.entity.Type;
import com.yalexin.service.BlogServiceImpl;
import com.yalexin.service.TypeServiceImpl;
import com.yalexin.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/types")
public class TypeShowController {
    @Autowired
    private TypeServiceImpl typeService;

    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping("/{id}")
    public String types(@PageableDefault(size = 6,sort = {"createTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @PathVariable("id") Long id, Model model){
        List<Type> types = typeService.listType(10000);
        if (id == -1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeId",id);
        return "types";
    }
}
