package com.yalexin.web.admin;

import com.yalexin.entity.Blog;
import com.yalexin.entity.Tag;
import com.yalexin.entity.User;
import com.yalexin.service.BlogServiceImpl;
import com.yalexin.service.JudgeEditedServiceImpl;
import com.yalexin.service.TagServiceImpl;
import com.yalexin.service.TypeServiceImpl;
import com.yalexin.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Random;


/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Controller
@RequestMapping("/admin/blogs")
public class BlogController {
    @Autowired
    BlogServiceImpl blogService;
    @Autowired
    TypeServiceImpl typeService;
    @Autowired
    TagServiceImpl tagService;
    @Autowired
    JudgeEditedServiceImpl judgeEditedService;

    @Value("#{'${pictrues.ps}'.split(',')}")
    private List<String>ptrs;

    // 访问 /admin/blogs 的时候，来到admin/blogs.html页面，即展示博客列表
    @GetMapping()
    public String blogs(@PageableDefault(size = 20, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs";
    }

    // 用户点击搜索按钮之后
    @PostMapping("/search")
    public String search(@PageableDefault(size = 20, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    // 博客新增页面
    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTAg());
        return "admin/blogs-input";
    }

    // 用户点击编辑按钮之后 来到博客修改页面 admin/blogs-edit.html
    @GetMapping("/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        Blog thisBlog = blogService.getBlog(id);
        thisBlog.init();
        model.addAttribute("blog", thisBlog);
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTAg());
        return "admin/blogs-edit";
    }

    // 用户将编写好的博客提交到后台，后台存入数据库后（如果可以存入）重定向到博客列表页面
    @PostMapping()
    public String post(Blog blog, BindingResult result, RedirectAttributes attributes, HttpSession session) {
        if (blog.getFirstPicture() == null ||  blog.getFirstPicture().equals("")){
            Random random = new Random();
            int randomIndex = random.nextInt(ptrs.size()) + 1;
            blog.setFirstPicture(ptrs.get(randomIndex));
        }
        User user = (User) session.getAttribute("user");
        blog.setUser(user);
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTAg(blog.getTagIds()));
        Blog blogByTitle = blogService.getBlogByTitle(blog.getTitle());
        if (blogByTitle != null) {
            result.rejectValue("title", "titleError", "已存在该标题的文章");
        }
        if (result.hasErrors()) {
            return "admin/blogs-input";
        }
        Blog save = blogService.saveBlog(blog);
        if (save == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/blogs";
    }

    // 用户将修改好的博客提交到后台，后台更新数据库后（如果可以存入）重定向到博客列表页面
    @PostMapping("/{id}")
    public String editPost(Blog blog, BindingResult result,
                           RedirectAttributes attributes,
                           HttpSession session,
                           @PathVariable("id") Long id) {
        Blog blogById = blogService.getBlog(id);
        System.out.println("----old----:" + blogById);
        if (blogById == null) {
            attributes.addFlashAttribute("message", "更新失败，没有找到该文章~");
        } else {
            if (!judgeEditedService.isEdited(blogById, blog)) {
                attributes.addFlashAttribute("message", "更新失败，您没有做任何修改");
                System.out.println("===================>>> 未作修改");
            }else{
                blogService.updateBlog(id,blog);
                attributes.addFlashAttribute("message", "更新成功");
            }
        }

        return "redirect:/admin/blogs";
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        return "redirect:/admin/blogs";
    }
}
