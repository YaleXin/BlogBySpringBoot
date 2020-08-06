package com.yalexin.web.admin;

import com.yalexin.entity.User;
import com.yalexin.service.UserService;
import com.yalexin.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String loginpage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model) {
        User user = userService.checkUser(username, MD5Utils.code(password));
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            model.addAttribute("userNickname",user.getNickname());
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名或者密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
//        将之前登录放进去的user清空
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
