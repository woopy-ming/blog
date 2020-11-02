package org.woopy.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.woopy.service.TagService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.Result;
import org.woopy.util.ResultGenerator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author woopy
 * @data 2020/8/18 - 19:44
 */
@Controller
@RequestMapping("/admin")
public class TagManagerController {

    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public String tagPage() {
        return "admin/tag";
    }

    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(tagService.getListByPage(pageUtil));
    }

    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody int[] ids){
        if (ids.length < 1)
            return ResultGenerator.genFailResult("参数异常！");

        if (tagService.delete(ids))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("存在关联关系，不可强行删除");
    }

    @PostMapping("tags/save")
    @ResponseBody
    public Result save(@RequestParam("tagName") String name){
        if (StringUtils.isEmpty(name))
            return ResultGenerator.genFailResult("参数异常！");

        if (tagService.save(name))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("标签名称重复");
    }
}
