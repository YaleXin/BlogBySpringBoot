package com.yalexin.web.admin;

import com.yalexin.entity.Type;
import com.yalexin.service.TypeServiceImpl;
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
@RequestMapping("/admin/types")
public class TypeController {
    @Autowired
    TypeServiceImpl typeService;

    /**
     * size : 每页大小 sort : 根据id排序 DESC : 降序
     * @param pageable 根据前端页面自动封装
     * @return
     */
//    @GetMapping("/types")
    @GetMapping()
    public String types(@PageableDefault(size = 10, sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    //    @GetMapping("/types/input")
    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }
    @GetMapping("/{id}/input")
    public String editInput(@PathVariable("id") Long id , Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-edit";
    }

    // attributes : 该变量可以在转发页面那边捕获
    // result : 接收校验结果
    @PostMapping
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type typeByname = typeService.getTypeByname(type.getName());
        if (typeByname != null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type save = typeService.
                save(type);
        if (save == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/types";
    }
    // @Valid Type type, BindingResult result 之间不能有其他变量
    @PostMapping("/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable("id") Long id,RedirectAttributes attributes) {
        Type typeByname = typeService.getTypeByname(type.getName());
        if (typeByname != null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type save = typeService.updateType(id,type);
        if (save == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
