package org.woopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.woopy.service.BlogService;

/**
 * @author woopy
 * @data 2020/8/24 - 13:38
 */
@Controller
public class ArchivesController {

    @Autowired
    BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archivesMap",blogService.archivesBlog());
        model.addAttribute("blogCount",blogService.getCountByStatus());
        return "archives";
    }
}
