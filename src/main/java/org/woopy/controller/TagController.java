package org.woopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.woopy.entity.Tag;
import org.woopy.service.BlogService;
import org.woopy.service.TagService;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/23 - 14:40
 */
@Controller
public class TagController {

    @Autowired
    TagService tagService;

    @Autowired
    BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tag(@PathVariable int id, Model model){
        List<Tag> tags = tagService.listTagTop(10000);
        if(id == -1)
            id = tags.get(0).getId();

        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.getBlogPageByTagIdAndName(id,1));
        model.addAttribute("activeId",id);

        return "tags";
    }
}
