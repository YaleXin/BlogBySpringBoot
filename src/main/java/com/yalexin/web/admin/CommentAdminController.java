package com.yalexin.web.admin;


import com.yalexin.entity.Comment;
import com.yalexin.service.CommentServiceImpl;
import com.yalexin.service.SendEmailServiceImpl;
import com.yalexin.vo.CommentQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/admin/comments")
public class CommentAdminController {
    @Autowired
    private CommentServiceImpl commentService;

    @Value("${comment_email_enable}")
    private boolean comment_email_enable;

    @Autowired
    private SendEmailServiceImpl sendEmailService;

    @GetMapping()
    public String comments(@PageableDefault(size = 10, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
        model.addAttribute("page", commentService.listComment(pageable));
        return "admin/comments";
    }
    // 通过审核或者下架评论
    @GetMapping("/audit/{id}")
    public String modified(@PathVariable Long id, RedirectAttributes attributes) {
        Comment newComment = new Comment();
        Comment comment = commentService.getComment(id);
        if (comment != null) {
            BeanUtils.copyProperties(comment, newComment);
            newComment.setAudited(!comment.isAudited());
            // 后台通过审核后 如果该条评论不是在文章下直接评论，且未发送过邮件，则发送邮件给父级评论者
            if(comment_email_enable && newComment.isAudited() && !comment.isSendEmailed() && comment.getParentComment() != null){
                sendEmailService.send(newComment.getParentComment(),newComment,true);
                newComment.setSendEmailed(true);
            }
            commentService.updateComment(id, newComment);
            attributes.addFlashAttribute("message", "操作成功");
        } else {
            attributes.addFlashAttribute("message", "操作失败");
        }
        return "redirect:/admin/comments";
    }

    // 用户点击搜索按钮之后
    @PostMapping("/search")public String search(@PageableDefault(size = 10, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                CommentQuery commentQuery, Model model) {
        Page<Comment> comments = commentService.listComment(pageable, commentQuery);
        model.addAttribute("page",comments );
        return "admin/comments :: commentList";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,Model model){
        Comment comment = commentService.getComment(id);
        model.addAttribute("comment",comment);
        return "admin/comments-edit";
    }

    @PostMapping("/{id}")
    public String editPost(Comment comment,@PathVariable("id") Long id,RedirectAttributes attributes){
        Comment oldComment = commentService.getComment(id);
        Comment targetComment = new Comment();
        BeanUtils.copyProperties(oldComment,targetComment);
        targetComment.setContent(comment.getContent());
        targetComment.setAudited(true);
        // 后台通过审核后 如果该条评论不是在文章下直接评论，且未发送过邮件，则发送邮件给父级评论者
        if (comment_email_enable && oldComment.getParentComment() != null && !oldComment.isSendEmailed()){
            sendEmailService.send(oldComment.getParentComment(),targetComment,true);
            targetComment.setSendEmailed(true);
        }
        commentService.updateComment(id,targetComment);
        attributes.addFlashAttribute("message", "编辑成功");
        return "redirect:/admin/comments";
    }
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        commentService.deleteComment(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/comments";
    }

}
