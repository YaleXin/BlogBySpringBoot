package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.dao.CommentRepository;
import com.yalexin.entity.Comment;
import com.yalexin.vo.CommentQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SendEmailServiceImpl sendEmailService;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by( "CreateTime");
        List<Comment> commentList = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(commentList);
    }

    @Transactional
    @CachePut(value = {"commentCache"},  key = "#result.id", condition ="#result!=null" )
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        // -1 就是顶层
        if (parentCommentId != -1) {
            Comment parentComment = commentRepository.findById(parentCommentId).get();
            comment.setParentComment(parentComment);
            // 发送给博主 通知审核
            sendEmailService.send(parentComment,comment,false);
        } else {
            comment.setParentComment(null);
            // parentComment : 防止报空指针异常
            Comment parentComment = new Comment();
            sendEmailService.send(parentComment,comment,false);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> listComment(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Cacheable(value = {"commentCache"}, key = "#id", condition = "#id>0")
    @Transactional
    @Override
    public Comment getComment(Long id) {
        Comment comment = null;
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()){
            comment = byId.get();
        }
        return comment;
    }
    @CachePut(value = {"commentCache"},  key = "#id", condition = "#result!=null")
    @Transactional
    @Override
    public Comment updateComment(Long id, Comment comment) {
        Comment oldComment = null;
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()){
            oldComment = byId.get();
        }
        if (oldComment == null){
            throw new NotFoundException("不存在该评论");
        }
        BeanUtils.copyProperties(comment,oldComment);
        return commentRepository.save(oldComment);
    }

    @Override
    public Page<Comment> listComment(Pageable pageable, CommentQuery commentQuery) {
        return commentRepository.findAll(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                if (commentQuery.getNickname() != null && !commentQuery.getNickname().equals("")){
                    predicates.add(cb.like(root.<String>get("nickname"), "%" + commentQuery.getNickname() + "%"));
                }
                if (commentQuery.getEmail() != null && !commentQuery.getEmail().equals("")){
                    predicates.add(cb.like(root.<String>get("email"), "%" + commentQuery.getEmail() + "%"));
                }
                if (commentQuery.isAudited()) {
                    predicates.add(cb.equal(root.<Boolean>get("audited"), commentQuery.isAudited()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }
    @Transactional
    @CacheEvict(value = {"commentCache"}, key = "#id")
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }


    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

}
