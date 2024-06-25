package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.SubscribeTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.SubscribeRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import com.gymmanagementsystembackend.tool.ServeTool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscribe")
@Tag(name = "场地预约管理接口")
public class SubScribeController {
    @Autowired
    private SubscribeRealize subscribeRealize;
    @Autowired
    private ServeTool serveTool;

    @Operation(summary = "添加预约信息")
    @PostMapping("/add")
    public ResultModel add(@RequestBody SubscribeTable subscribeTable){
        subscribeRealize.add(subscribeTable);
        return Inf.unify(Code.subscribe_ok,null,"预约成功!");
    }

    @Operation(summary = "删除预约信息",description = "可按行id删除")
    @DeleteMapping("/delete")
    public ResultModel delete(@RequestParam int id){
        subscribeRealize.delete(id);
        return Inf.unify(Code.subscribe_ok,null,"取消成功!");
    }

    @Operation(summary = "查询已预约会员",description = "可按会员名查询")
    @PostMapping("/get/subscribevip")
    public ResultModel getSubScribeVip(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=subscribeRealize.getPage(getPageModel);
        return Inf.unify(Code.subscribe_ok,sendPageModel,null);
    }

    @Operation(summary = "查询可预约课程",description = "可按课程和教练名查询")
    @PostMapping("/get/subscribeclass")
    public ResultModel getSubScribeClass(@RequestBody GetPageModel getPageModel){
        serveTool.deleteExpiredCourses();
        SendPageModel sendPageModel=subscribeRealize.getSubScribe(getPageModel);
        return Inf.unify(Code.subscribe_ok,sendPageModel,null);
    }
}
