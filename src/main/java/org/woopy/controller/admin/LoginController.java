package org.woopy.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.woopy.entity.User;
import org.woopy.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author woopy
 * @data 2020/8/17 - 13:33
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    TypeService typeService;

    @Autowired
    CommentService commentService;

    @Autowired
    TagService tagService;

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        Model model,HttpSession session){
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            model.addAttribute("errorMsg","用户名或密码不能为空");
            return "admin/login";
        }

        User user =  userService.login(userName,password);
        if (user != null){
            session.setAttribute("user",user);
            session.setMaxInactiveInterval(60 * 60 *2);
            return "redirect:/admin/index";
        }else {
            model.addAttribute("errorMsg","登录失败");
            return "admin/login";
        }

    }

    @GetMapping({"","/","/index"})
    public String index(HttpServletRequest request){
        request.setAttribute("blogCount",blogService.getTotalCount());
        request.setAttribute("categoryCount",typeService.getTotalCount());
        request.setAttribute("commentCount",commentService.getTotalCount());
        request.setAttribute("tagCount",tagService.getTotalCount());
        return "admin/index";
    }

    @GetMapping("/profile")
    public String profile(){
        return "admin/profile";
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(@RequestParam("loginUserName") String userName,
                             @RequestParam("nickName") String nickName,
                             HttpServletRequest request){
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (userService.updateName(user.getId(),userName,nickName))
            return "success";
        else
            return "修改失败";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                             @RequestParam("newPassword") String newPassword){
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (userService.updatePassword(user.getId(),originalPassword,newPassword)){
            return "success";
        }
        else
            return "修改失败";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "admin/login";
    }


}
