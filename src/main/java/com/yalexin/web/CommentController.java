package com.yalexin.web;

import com.yalexin.entity.Blog;
import com.yalexin.entity.Comment;
import com.yalexin.entity.User;
import com.yalexin.service.BlogServiceImpl;
import com.yalexin.service.CommentServiceImpl;
import com.yalexin.service.SendEmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
public class CommentController {
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    private BlogServiceImpl blogService;
    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    @PostMapping("comments")
    public String post(Comment comment, HttpSession session, RedirectAttributes attributes) {
        Long blogId = comment.getBlog().getId();
        Blog blog = blogService.getBlog(blogId);
        comment.setBlog(blog);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
        if (blog.isCommentabled()) {
            commentService.saveComment(comment);
        }
        attributes.addFlashAttribute("message", "评论成功！请耐心等待管理员审核。");
        return "redirect:/comments/" + blogId;
    }
}
