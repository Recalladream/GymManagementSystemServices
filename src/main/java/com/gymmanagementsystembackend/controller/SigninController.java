package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.SigninTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.model.SigninModel;
import com.gymmanagementsystembackend.serve.SigninRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/signin")
@Tag(name = "签到管理接口")
public class SigninController {
    @Autowired
    private SigninRealize signinRealize;
    @Operation(summary = "添加签到")
    @PostMapping("/sign")
    public ResultModel vipsign(@RequestBody SigninTable signinTable){
        signinRealize.sign(signinTable);
        return Inf.unify(Code.signin_ok,null,"签到成功！");
    }
    @Operation(summary = "查询签到信息",description = "在上课课程中点击查看后，调用此接口，要传sign:已签|未签,可按会员名查询")
    @PostMapping("/get/signinf")
    public ResultModel getSignInf(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=signinRealize.getSignInf(getPageModel);
        return Inf.unify(Code.signin_ok,sendPageModel,null);
    }

    @Operation(summary = "查询上课课程",description = "可按课程和教练名查询")
    @PostMapping("/get/signcourse")
    public ResultModel getSignCourse(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=signinRealize.getSignCourse(getPageModel);
        return Inf.unify(Code.signin_ok,sendPageModel,null);
    }

    @Operation(summary = "获取签到码")
    @PostMapping("/get/signincode")
    public ResultModel getSigNinCode(@RequestBody SigninModel signinModel){
        String sigNinCode=signinRealize.getSigNinCode(signinModel);
        return Inf.unify(Code.signin_ok,sigNinCode,"生成签到码成功!");
    }
}
