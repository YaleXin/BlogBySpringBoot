package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.dao.BlogRepository;
import com.yalexin.entity.Blog;
import com.yalexin.entity.Type;
import com.yalexin.entity.User;
import com.yalexin.util.MarkdownUtils;
import com.yalexin.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private TypeServiceImpl typeService;

    @Cacheable(value = {"blogCache"}, key = "#id", condition = "#id>0")
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        Blog blog = null;
        Optional<Blog> optional = blogRepository.findById(id);
        if (optional.isPresent()) {
            blog = optional.get();
        }
        System.out.println("=== 请求了" + id +  "博客 ===");
        if (blog == null) {
            throw new NotFoundException("不存在该博客文章");
        }
        return blog;
    }


    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root,
                                         CriteriaQuery<?> cq,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (blog.getTitle() != null && !blog.getTitle().equals("")) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>(){

            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq,
                                         CriteriaBuilder cb) {
                Join<Object, Object> join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Map<String, List<Blog>> archivesBlog() {
        List<String> groupYear = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new LinkedHashMap<>();
        for (String year :
                groupYear) {
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }
    @CachePut(value = {"blogCache"},  key = "#result.id", condition ="#result!=null" )
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getCreateTime() != null){
            blog.setUpdateTime(new Date(blog.getCreateTime().getTime()));
        }else {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
        }
        System.out.println("=== 存入了" + blog.getId() +  "博客 ===");
        blog.setViews(0);
        return blogRepository.save(blog);
    }
    @CachePut(value = {"blogCache"},  key = "#id", condition = "#result!=null")
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog oldBlog = null;
        Optional<Blog> optional = blogRepository.findById(id);
        if (optional.isPresent()) {
            oldBlog = optional.get();
        }
        System.out.println("=== 更新了" + id +  "博客 ===");
        if (oldBlog == null) {
            throw new NotFoundException("不存在该博客文章");
        }
        oldBlog2newBlog(oldBlog, blog);
        oldBlog.setUpdateTime(new Date());
        return blogRepository.save(oldBlog);
    }
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageRequest = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageRequest);
    }

    private void oldBlog2newBlog(Blog oldBlog, Blog newBlog) {
        oldBlog.setFlag(newBlog.getFlag());
        oldBlog.setTitle(newBlog.getTitle());
        oldBlog.setContent(newBlog.getContent());
        oldBlog.setFirstPicture(newBlog.getFirstPicture());
        oldBlog.setDescription(newBlog.getDescription());
        oldBlog.setCreateTime(newBlog.getCreateTime());

        oldBlog.setRecommend(newBlog.isRecommend());
        oldBlog.setShareStatement(newBlog.isShareStatement());
        oldBlog.setAppreciation(newBlog.isAppreciation());
        oldBlog.setCommentabled(newBlog.isCommentabled());
        oldBlog.setPublished(newBlog.isPublished());

        newBlog.setType(typeService.getType(newBlog.getType().getId()));
        newBlog.setTags(tagService.listTAg(newBlog.getTagIds()));
        oldBlog.setType(newBlog.getType());
        oldBlog.setTags(newBlog.getTags());
    }

    @CacheEvict(value = {"blogCache"}, key = "#id")
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Blog getBlogByTitle(String title) {
        return blogRepository.findByTitle(title);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
