package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.dao.TagRepository;
import com.yalexin.entity.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

    @CachePut(value = {"tagCache"}, key = "#result.id", condition = "#result!=null")
    @Transactional
    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Cacheable(value = {"tagCache"}, key = "#id", condition = "#id>0")
    @Transactional
    @Override
    public Tag getTag(Long id) {
        //    2.0 以下版本：    return tagRepository.findOne(id);
        Tag tag = null;
        Optional<Tag> optional = tagRepository.findById(id);
        if (optional.isPresent()) {
            tag = optional.get();
        }
        return tag;
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTAg() {
        return tagRepository.findAll();
    }

    // 将前端传过来的类似1，2，3，4 转为相应的标签list
    private List<Long> convetToList(String ids) {
        List<Long> list = new ArrayList<>();

        // 用正则表达式判断字符串是否只有数字 只有数字组成的就是数据库中已经存在
        Pattern pattern = Pattern.compile("[0-9]*");

        if (ids != null && !"".equals(ids)) {
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {

                if (pattern.matcher(split[i]).matches()) {
                    list.add(new Long(split[i]));
                } else {
                    Tag tag = new Tag();
                    tag.setId(null);
                    tag.setName(split[i]);
                    Tag save = tagRepository.save(tag);
                    list.add(new Long(save.getId()));
                }
            }
        }
        return list;
    }

    @Override
    public List<Tag> listTAg(String ids) {
        return tagRepository.findAllById(convetToList(ids));
    }

    @Override
    public List<Tag> listTAg(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageRequest = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageRequest);
    }

    @CachePut(value = {"tagCache"}, key = "#id", condition = "#result!=null")
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag oldTag = null;
        Optional<Tag> optional = tagRepository.findById(id);
        if (optional.isPresent()) {
            oldTag = optional.get();
        }
        if (oldTag == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, oldTag);
        return tagRepository.save(oldTag);
    }

    @CacheEvict(value = {"tagCache"}, key = "#id")
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }
}
