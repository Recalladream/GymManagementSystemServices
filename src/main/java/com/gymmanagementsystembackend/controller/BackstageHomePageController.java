package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.model.BackstageHomePageDataModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.serve.BackstageHomePageRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backstage/homepage")
@Tag(name = "后台首页管理接口")
public class BackstageHomePageController {
    @Autowired
    private BackstageHomePageRealize backstageHomePageRealize;

    @Operation(summary = "后台数据统计")
    @GetMapping("/data")
    public ResultModel getBackstageHomePageData(){
        BackstageHomePageDataModel bhpd=backstageHomePageRealize.getBackgroundStatistics();
        return Inf.unify(Code.backstage_homepage_data_ok,bhpd,null);
    }
}
