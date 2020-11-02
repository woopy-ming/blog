package org.woopy.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.woopy.util.Result;
import org.woopy.util.ResultGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author woopy
 * @data 2020/8/25 - 14:00
 */
@Controller
@RequestMapping("/admin")
public class UploadController {

    @PostMapping("/upload/file")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file){
        if (file.isEmpty())
            return ResultGenerator.genFailResult("文件上传失败");

        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String finalFilename = sdf.format(new Date()) + suffixName;
        File dest = new File(getUpload() + finalFilename);

        try {
            file.transferTo(dest);
            Result successResult = ResultGenerator.genSuccessResult();
            successResult.setData("/upload/" + finalFilename);
            return successResult;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    @PostMapping("/blogs/md/uploadfile")
    public void uploadFileByEditorMd(HttpServletRequest request,
                                     HttpServletResponse response, @RequestParam(name = "editormd-image-file", required = true)
                                                 MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String finalFilename = sdf.format(new Date()) + suffixName;
        File dest = new File(getUpload() + finalFilename);

        try {
            file.transferTo(dest);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + "/upload/"+finalFilename + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":0}");
        }
    }

    private String getUpload(){
        String path = System.getProperty("user.dir") + "/src/main/resources/static/upload/";
        return path;
    }
}
