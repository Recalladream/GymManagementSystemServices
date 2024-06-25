package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.tool.Inf;
import com.gymmanagementsystembackend.tool.UpLoadOss;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
@Tag(name = "OSS管理接口")
public class AliyunOssController {
    @Autowired
    private UpLoadOss upLoadOss;

    @Operation(summary = "上传OSS")
    @PostMapping("/upload")
    public ResultModel uploadOss(MultipartFile file){
        String url=upLoadOss.upload(file);
        return Inf.unify("12-01-01",url,"上传成功");
    }
}
