package com.yalexin.web.admin;

import com.yalexin.entity.Tag;
import com.yalexin.service.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/admin/tags")
public class TagController {
    @Autowired
    TagServiceImpl tagService;

    @GetMapping()
    public String tags(@PageableDefault(size = 10, sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }
    @GetMapping("/{id}/input")
    public String editInput(@PathVariable("id") Long id , Model model) {
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-edit";
    }

    @PostMapping()
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag tagByname = tagService.getTagByName(tag.getName());
        if (tagByname != null) {
            result.rejectValue("name", "nameError", "不能添加重复的标签");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag save = tagService.save(tag);
        if (save == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }
    @PostMapping("/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable("id") Long id, RedirectAttributes attributes) {
        Tag tagByname = tagService.getTagByName(tag.getName());
        if (tagByname != null) {
            result.rejectValue("name", "nameError", "不能添加重复的标签");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag save = tagService.updateTag(id,tag);
        if (save == null) {
            attributes.addFlashAttribute("message", "修改失败");
        } else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/admin/tags";
    }
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
