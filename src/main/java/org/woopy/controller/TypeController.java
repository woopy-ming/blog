package org.woopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.woopy.entity.Type;
import org.woopy.service.BlogService;
import org.woopy.service.TypeService;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/23 - 11:06
 */
@Controller
public class TypeController {

    @Autowired
    TypeService typeService;

    @Autowired
    BlogService blogService;

    @GetMapping("/types")
    public String getTypePage(){
        return "types";
    }

    @GetMapping("/types/{id}")
    public String gettypeId(@PathVariable int id, Model model){
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1)
            id = types.get(0).getId();
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.getBlogPageByTypeId(id,1));
        model.addAttribute("activeId",id);
        return "types";
    }
}
