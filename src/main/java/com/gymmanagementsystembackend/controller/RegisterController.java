package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.RegisterTable;
import com.gymmanagementsystembackend.model.RegisterModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.serve.RegisterRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/register")
@Tag(name = "注册管理接口")
public class RegisterController {
    @Autowired
    private RegisterRealize registerRealize;

    @Operation(summary = "账号注册")
    @PostMapping("/account")
    public ResultModel registerAccount(@RequestBody RegisterModel registerModel){
        registerRealize.registerUserAccount(registerModel);
        return Inf.unify(Code.register_ok,"","注册成功！");
    }
}
