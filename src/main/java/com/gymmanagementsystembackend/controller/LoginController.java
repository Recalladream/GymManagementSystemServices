package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.RegisterTable;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.UserLoginModel;
import com.gymmanagementsystembackend.serve.LoginRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/login")
@Tag(name = "登录管理接口")
public class LoginController {
    @Autowired
    private LoginRealize loginRealize;

    @Operation(summary = "账号登录")
    @PostMapping("/user/account")
    public ResultModel loginAccount(@RequestBody RegisterTable registerTable){
        UserLoginModel userLoginModel=loginRealize.loginUserAccount(registerTable.getAccount(),registerTable.getPassword());
        return Inf.unify(Code.login_ok,userLoginModel,"登录成功！");
    }

    @Operation(summary = "管理员登录")
    @PostMapping("/manager/account")
    public ResultModel loginManagerAccount(@RequestBody RegisterTable registerTable){
        String token=loginRealize.loginManagerAccount(registerTable.getAccount(),registerTable.getPassword());
        return Inf.unify(Code.login_ok,token,"登录成功！");
    }

    @Operation(summary = "手机登录")
    @GetMapping("/user/phone")
    public ResultModel loginUserPhone(@RequestParam String phone, @RequestParam String code){
        UserLoginModel userLoginModel=loginRealize.loginUserPhone(phone,code);
        return Inf.unify(Code.login_ok,userLoginModel,"登陆成功！");
    }

    @Operation(summary = "获取手机号登录验证码",description = "120秒过期")
    @GetMapping("/user/phone/code")
    public ResultModel getPhoneLoginCode(@RequestParam String phone){
        loginRealize.getPhoneLoginCode(phone);
        return Inf.unify(Code.get_code_ok,null,"获取验证码成功！注意查收");
    }
    @Operation(summary = "获取账号注册验证码",description = "120秒过期")
    @GetMapping("/user/account/code")
    public ResultModel getAccountRegisterCode(@RequestParam String phone){
        loginRealize.getAccountRegisterCode(phone);
        return Inf.unify(Code.get_code_ok,null,"获取验证码成功！注意查收");
    }

}
