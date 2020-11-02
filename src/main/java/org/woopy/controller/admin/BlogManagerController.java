package org.woopy.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.woopy.entity.Blog;
import org.woopy.service.BlogService;
import org.woopy.service.TypeService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.Result;
import org.woopy.util.ResultGenerator;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author woopy
 * @data 2020/8/17 - 20:21
 */
@Controller
@RequestMapping("/admin")
public class BlogManagerController {

    @Autowired
    BlogService blogService;

    @Autowired
    TypeService typeService;

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        /*System.out.println("输出");
        System.out.println(params.toString());*/

        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(blogService.getBlogsPage(pageUtil));
    }

    @GetMapping("/blogs")
    public String list(HttpServletRequest request) {
        //request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    @GetMapping("/blogs/edit")
    public String edit(HttpServletRequest request){
        request.setAttribute("categories",typeService.getAllType());
        return "admin/edit";
    }

    @GetMapping("/blogs/edit/{id}")
    public String edit(@PathVariable("id") int blogId,HttpServletRequest request){
        Blog blog = blogService.getBlogById(blogId);
        if (blog == null)
            return "error/404";

        request.setAttribute("blog",blog);
        request.setAttribute("categories",typeService.getAllType());
        return "admin/edit";
    }

    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle") String blogTitle,
                       @RequestParam("description") String description,
                       @RequestParam("blogTypeId") Integer blogTypeId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Byte blogStatus,
                       @RequestParam("flag") int flag){
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        //System.out.println("拿到的简介是 "+description);
        if (StringUtils.isEmpty(description)){
            description = "一个不算是简介的简介";
        }

        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setDescription(description);
        blog.setBlogTypeId(blogTypeId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        //System.out.println("内容是->  \n"+blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setFlag(flag);


        String saveResult = blogService.saveBlog(blog);
        if ("success".equals(saveResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveResult);
        }
    }

    @PostMapping("/blogs/update")
    @ResponseBody
    public Result update(@RequestParam("blogId") int blogId,
                         @RequestParam("description") String description,
                         @RequestParam("blogTitle") String blogTitle,
                         @RequestParam("blogTypeId") Integer blogTypeId,
                         @RequestParam("blogTags") String blogTags,
                         @RequestParam("blogContent") String blogContent,
                         @RequestParam("blogCoverImage") String blogCoverImage,
                         @RequestParam("blogStatus") Byte blogStatus,
                         @RequestParam("flag") int flag){
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        //System.out.println("拿到的简介是 "+description);
        if (StringUtils.isEmpty(description)){
            description = "一个不算是简介的简介";
        }

        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setDescription(description);
        blog.setBlogTitle(blogTitle);
        blog.setBlogTypeId(blogTypeId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        //System.out.println("内容是->  \n"+blogCoverImage);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setUpdateTime(new Date());
        blog.setFlag(flag);

        String updateResult = blogService.updateBlog(blog);
        if ("success".equals(updateResult)) {
            return ResultGenerator.genSuccessResult("修改成功");
        } else {
            return ResultGenerator.genFailResult(updateResult);
        }
    }

    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result delete(@RequestBody int[] ids){
        if (ids.length < 1)
            return ResultGenerator.genFailResult("参数异常！");

        if (blogService.deleteBatch(ids))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("删除失败");
    }

}
