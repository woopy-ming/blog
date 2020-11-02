package org.woopy.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.woopy.entity.Blog;
import org.woopy.service.BlogService;
import org.woopy.service.TagService;
import org.woopy.service.TypeService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author woopy
 * @data 2020/8/16 - 12:34
 */
@Controller
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    TypeService typeService;

    @Autowired
    TagService tagService;

    @GetMapping("/blog/{id}")
    public String getblog(@PathVariable int id, Model model){
        Blog blog = blogService.getBlogAndTagById(id);
        model.addAttribute("blog",blog);
        return "blog";
    }

    @GetMapping({"","/","index.html","index"})
    public String index(Model model){
        PageInfo page = blogService.getBlogList(1);
        model.addAttribute("page",page);
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        return "index";
    }

    @GetMapping("/page/{pageNum}")
    public String getPage(@PathVariable("pageNum") int pageNum,Model model){
        PageInfo page = blogService.getBlogList(pageNum);
        model.addAttribute("page",page);
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        return "index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/search")
    public String search(@RequestParam("text") String text,Model model){
        PageInfo pageInfo = blogService.getBlogsBytitle(1, text);
        model.addAttribute("page" ,pageInfo);
        model.addAttribute("text",text);

        return "search";
    }

}
