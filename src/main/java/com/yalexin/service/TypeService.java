package com.yalexin.service;

import com.yalexin.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type save(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listType(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);

    Type getTypeByname(String name);
}
