package com.yalexin.web;

import com.yalexin.service.BlogService;
import com.yalexin.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/archives")
public class ArchiveShowController {
    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping()
    public String archives(Model model) {
        model.addAttribute("archieveMap", blogService.archivesBlog());
        model.addAttribute("blogCount",blogService.countBlog());
        return "archives" ;
    }
}
