package com.yalexin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/timeline")
public class TimelineController {
    @RequestMapping
    public String timeline(){
        return "timeline";
    }
}
