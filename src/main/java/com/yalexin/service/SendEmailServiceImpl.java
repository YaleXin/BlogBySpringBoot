package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.entity.Blog;
import com.yalexin.entity.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
@Component
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${blog_host}")
    private String blog_host;

    @Value("${blogger_nickname}")
    private String blogger_nickname;

    @Value("${emailfrom}")
    private String email_from;

    @Value("${email_receive}")
    private String email_receive;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Async
    @Override
    public void send(Comment parentComment, Comment replyComment, boolean is2Parent) {
        if (parentComment == null) {
            throw new NotFoundException("不存在该评论");
        } else {
            String parentCommentEmail = parentComment.getEmail();
            String parentCommentNickname = parentComment.getNickname();
            Blog blog = replyComment.getBlog();


            Context context = new Context();
            context.setVariable("parentCommentNickname", parentCommentNickname);
            context.setVariable("blogTitle", blog.getTitle());
            context.setVariable("parentContent", parentComment.getContent());

            context.setVariable("replyNickname", replyComment.getNickname());
            context.setVariable("replyContent", replyComment.getContent());
            context.setVariable("replyAddr", blog_host
                    + "/blog/" + blog.getId() + "/#reply-comment-" + replyComment.getId());
            context.setVariable("blogHost", blog_host);
            context.setVariable("bloggernickname", blogger_nickname);
            String emailTemplate = null;

            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(email_from);

                // 如果是给父级评论者发送邮件
                if (is2Parent) {
                    helper.setTo(parentCommentEmail);
                    helper.setSubject("您在[" + blogger_nickname + "]上的评论有新的回复啦！");
                    emailTemplate = templateEngine.process("emailTemplate", context);

                } else {
                    helper.setTo(email_receive);
                    helper.setSubject("您在[" + blogger_nickname + "]上的文章有新的回复啦！");
                    context.setVariable("replyAddr", blog_host
                            + "/admin/comments");
                    emailTemplate = templateEngine.process("toSelfEmailTemplate", context);
                }
                helper.setText(emailTemplate, true);
                javaMailSender.send(message);
                logger.info("send email succeed! from {} to {}",email_from,is2Parent ? parentCommentEmail : email_receive);
            } catch (MessagingException e) {
                logger.error("send email failed! from {} to {} , StackTrace : {}, message: {}",
                        email_from,is2Parent ? parentCommentEmail : email_receive, e.getStackTrace(), e.getMessage());
            }

        }
    }

}
