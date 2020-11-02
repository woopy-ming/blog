package org.woopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.woopy.entity.Comment;
import org.woopy.entity.User;
import org.woopy.service.BlogService;
import org.woopy.service.CommentService;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable int blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogId(blogId);

        /*System.out.println("循环后");
        for (Comment c:comments)
            System.out.println(c.toString());*/

        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        int blogId = comment.getBlog().getBlogId();
        System.out.println("获取到的"+blogId);
        comment.setBlog(blogService.getBlogById(blogId));
        comment.setAvatar(avatar);
        User user = (User) session.getAttribute("user");
       /* if (user != null) {
            comment.setStatus(2);
            if (commentService.saveComment(comment) > 0)
                return "success";
            else
                return "error";
        }
        if (commentService.saveComment(comment) > 0)
            return "success";
        else
            return "error";*/
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }



}
