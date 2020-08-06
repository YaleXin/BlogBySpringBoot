package com.yalexin.web;

import com.yalexin.config.FriendsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/links")
public class FriendsController {
    @Autowired
    FriendsConfig friendsConfig;

    @RequestMapping()
    public String friends(Model model) {
        model.addAttribute("friends", friendsConfig.getFriends());
        return "links";
    }
}
