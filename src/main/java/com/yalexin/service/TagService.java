package com.yalexin.service;

import com.yalexin.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag save(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTAg();
    // 将前端传来的标签字符串（例如 1,2,3,4）转成相应的标签对象并且返回
    List<Tag>listTAg(String ids);

    List<Tag> listTAg(Integer size);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag getTagByName(String name);
}
