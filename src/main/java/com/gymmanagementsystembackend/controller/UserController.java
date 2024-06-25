package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.SubscribeTable;
import com.gymmanagementsystembackend.exception.BusinessException;
import com.gymmanagementsystembackend.model.*;
import com.gymmanagementsystembackend.serve.SubscribeRealize;
import com.gymmanagementsystembackend.serve.UserRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理接口")
public class UserController {
    @Autowired
    private UserRealize userRealize;
    @Autowired
    private SubscribeRealize subscribeRealize;

    @Operation(summary = "用户预约课程")
    @PostMapping("/booked/course")
    public ResultModel userBookedCourse(@RequestBody SubscribeTable subscribeTable){
        if (subscribeTable.getVipId()==null||"".equals(subscribeTable.getVipId())){
            throw new BusinessException(Code.subscribe_fail,"会员专享!");
        }
        subscribeRealize.add(subscribeTable);
        return Inf.unify(Code.subscribe_ok,null,"预约成功!");
    }

    @Operation(summary = "修改用户个人信息")
    @PostMapping("/update/introduce")
    public ResultModel userUpdateIntroduce(@RequestBody UserModel userModel){
        userRealize.updateUser(userModel);
        return Inf.unify(Code.subscribe_ok,null,"修改成功!");
    }

    @Operation(summary = "查询用户个人信息")
    @GetMapping("/get/userinf")
    public ResultModel getUserInf(@RequestParam String vipId){
        UserModel userModel=userRealize.getUserInf(vipId);
        return Inf.unify(Code.user_ok,userModel,null);
    }

    @Operation(summary = "查询用户已预约课程")
    @PostMapping("/get/yibooked")
    public ResultModel getYiBooked(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=userRealize.getYiBooked(getPageModel);
        return Inf.unify(Code.subscribe_ok,sendPageModel,null);
    }

    @Operation(summary = "用户开通会员")
    @PostMapping("/open/vip")
    public ResultModel openVip(@RequestBody VipModel vipModel){
        String vipId=userRealize.openVip(vipModel);
        return Inf.unify(Code.vip_ok,vipId,"恭喜您已开通会员成功！");
    }

    @Operation(summary = "用户签到")
    @PostMapping("/signin")
    public ResultModel signin(@RequestBody SigninModel signinModel){
        userRealize.UserSigNin(signinModel);
        return Inf.unify(Code.signin_ok,null,"签到成功");
    }

}
