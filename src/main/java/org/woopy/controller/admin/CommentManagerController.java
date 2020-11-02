package org.woopy.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.woopy.service.CommentService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.Result;
import org.woopy.util.ResultGenerator;

import java.util.Map;

/**
 * @author woopy
 * @data 2020/8/19 - 17:01
 */
@Controller
@RequestMapping("/admin")
public class CommentManagerController {

    @Autowired
    CommentService commentService;

    @GetMapping("/comments")
    public String getcommentPage(){
        return "admin/comment";
    }

    @GetMapping("/comments/list")
    @ResponseBody
    public Result commentList(@RequestParam Map<String,Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(commentService.getCommentsPage(pageUtil));
    }

    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody int[] ids){
        if (ids.length < 1)
            return ResultGenerator.genFailResult("参数异常");

        if (commentService.checkDone(ids))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("审核失败");
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody int[] ids){
        if (ids.length < 1)
            return ResultGenerator.genFailResult("参数异常");

        if (commentService.deleteBatch(ids))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("删除失败");
    }

}
