package org.woopy.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.woopy.service.TypeService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.Result;
import org.woopy.util.ResultGenerator;

import java.util.Map;

/**
 * @author woopy
 * @data 2020/8/19 - 17:15
 */
@Controller
@RequestMapping("/admin")
public class TypeManagerController {

    @Autowired
    TypeService typeService;

    @GetMapping("/categories")
    public String typePage(){
        return "admin/category";
    }


    @GetMapping("/categories/list")
    @ResponseBody
    public Result typeList(@RequestParam Map<String,Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(typeService.getAllList(pageUtil));

    }

    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String name,
                       @RequestParam("categoryIcon") String icon){
        if (StringUtils.isEmpty(name)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(icon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }

        if (typeService.save(name,icon))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("分类名称重复");
    }

    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryId") int id,
                         @RequestParam("categoryName") String name,
                         @RequestParam("categoryIcon") String icon){
        if (StringUtils.isEmpty(name)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(icon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }

        if (typeService.update(id,name,icon))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("分类名称重复");
    }

    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody int[] ids){
        if (ids.length < 1)
            return ResultGenerator.genFailResult("参数异常！");

        if (typeService.delete(ids))
            return ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("删除失败");
    }
}
