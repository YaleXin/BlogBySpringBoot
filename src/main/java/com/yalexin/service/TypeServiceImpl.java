package com.yalexin.service;

import com.yalexin.NotFoundException;
import com.yalexin.dao.TypeRepository;
import com.yalexin.entity.Type;
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

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    TypeRepository typeRepository;

    @CachePut(value = {"typeCache"}, key = "#result.id", condition = "#result!=null")
    @Transactional
    @Override
    public Type save(Type type) {
        return typeRepository.save(type);
    }

    @Cacheable(value = {"typeCache"}, key = "#id", condition = "#id>0")
    @Transactional
    @Override
    public Type getType(Long id) {

//    2.0 以下版本：    return typeRepository.findOne(id);
        Type type = null;
        Optional<Type> optional = typeRepository.findById(id);
        if (optional.isPresent()) {
            type = optional.get();
        }
        return type;
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listType(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageRequest = PageRequest.of(0, size, sort);
        return typeRepository.findTop(pageRequest);
    }

    @CachePut(value = {"typeCache"}, key = "#id", condition = "#result!=null")
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type oldType = null;
        Optional<Type> optional = typeRepository.findById(id);
        if (optional.isPresent()) {
            oldType = optional.get();
        }
        if (oldType == null) {
            throw new NotFoundException("不存在该分类");
        }
        BeanUtils.copyProperties(type, oldType);
        return typeRepository.save(oldType);
    }

    @CacheEvict(value = {"typeCache"}, key = "#id")
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public Type getTypeByname(String name) {
        return typeRepository.findByName(name);
    }
}
