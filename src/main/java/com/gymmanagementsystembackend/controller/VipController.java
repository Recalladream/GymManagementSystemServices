package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.model.*;
import com.gymmanagementsystembackend.serve.VipRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vip")
@Tag(name = "会员管理接口")
public class VipController {
    @Autowired
    private VipRealize vipRealize;

    @Operation(summary = "添加会员")
    @PostMapping("/add")
    public ResultModel addVip(@RequestBody VipModel vipModel){
        vipRealize.addVipUser(vipModel);
        return Inf.unify(Code.vip_add_ok,null,"添加成功！");
    }

    @Operation(summary = "删除会员",description = "按会员id删除")
    @DeleteMapping("/delete/one")
    public ResultModel deleteOneVip(@RequestParam String vipId){
        vipRealize.deleteVipOne(vipId);
        return Inf.unify(Code.vip_delete_ok,null,"删除成功！");
    }

    @Operation(summary = "修改会员基本信息",description = "如姓名、年龄、性别..")
    @PutMapping("/updete/user")
    public ResultModel updateVipUser(@RequestBody VipModel vipModel){
        vipRealize.updateVipUser(vipModel);
        return Inf.unify(Code.vip_update_ok,null,"修改成功！");
    }

    @Operation(summary = "修改会籍信息",description = "只能该会员类型")
    @PutMapping("/updete/inf")
    public ResultModel updateVipInf(@RequestBody VipModel vipModel){
        vipRealize.updateVipInf(vipModel);
        return Inf.unify(Code.vip_update_ok,null,"修改成功！");
    }

    @Operation(summary = "修改会员健身数据",description = "如身高、体重、血压")
    @PutMapping("/updete/fitness")
    public ResultModel updateVipFitness(@RequestBody VipModel vipModel){
        System.out.println(vipModel);
        vipRealize.updateVipFitness(vipModel);
        return Inf.unify(Code.vip_update_ok,null,"修改成功！");
    }

    @Operation(summary = "查询会员列表",description = "可按会员名查询")
    @PostMapping("/get/page")
    public ResultModel getPageVip(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=vipRealize.getPageVip(getPageModel);
        return Inf.unify(Code.vip_ok,sendPageModel,null);
    }

    @Operation(summary = "会员续费",description = "续费时间在1-12月")
    @PostMapping("/renew")
    public ResultModel renew(@RequestBody RenewModel renewModel){
        vipRealize.renew(renewModel);
        return Inf.unify(Code.vip_ok,null,"续费成功！");
    }
}
