package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.MaterialTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.MaterialRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/material")
@Tag(name = "器材管理接口")
public class MaterialController {
    @Autowired
    private MaterialRealize materialRealize;
    @Operation(summary = "添加完整器材")
    @PostMapping("/add/well")
    public ResultModel addWell(@RequestBody MaterialTable materialTable){
        materialRealize.addWell(materialTable);
        return Inf.unify(Code.material_ok,null,"添加成功!");
    }
    @Operation(summary = "添加损坏器材")
    @PostMapping("/add/bad")
    public ResultModel addbad(@RequestBody MaterialTable materialTable){
        materialRealize.addBad(materialTable);
        return Inf.unify(Code.material_ok,null,"添加成功!");
    }

    @Operation(summary = "删除完整器材",description = "按器材id删除")
    @DeleteMapping("/delete/well")
    public ResultModel deletewell(@RequestParam String materialId){
        materialRealize.deleteWell(materialId);
        return Inf.unify(Code.material_ok,null,"删除成功!");
    }

    @Operation(summary = "删除损坏器材",description = "按器材id删除")
    @DeleteMapping("/delete/bad")
    public ResultModel deletebad(@RequestParam String materialId){
        materialRealize.deleteBad(materialId);
        return Inf.unify(Code.material_ok,null,"删除成功!");
    }

    @Operation(summary = "修改完整器材")
    @PutMapping("/update/well")
    public ResultModel update(@RequestBody MaterialTable materialTable){
        materialRealize.updateWell(materialTable);
        return Inf.unify(Code.material_ok,null,"修改成功!");
    }

    @Operation(summary = "修改损坏器材")
    @PutMapping("/update/bad")
    public ResultModel updatebad(@RequestBody MaterialTable materialTable){
        materialRealize.updateBad(materialTable);
        return Inf.unify(Code.material_ok,null,"修改成功!");
    }

    @Operation(summary = "查询完整器材",description = "可按器材名查询")
    @PostMapping("/get/well")
    private ResultModel getwell(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=materialRealize.getWellPage(getPageModel);
        return Inf.unify(Code.material_ok,sendPageModel,null);
    }

    @Operation(summary = "查询损坏器材",description = "可按器材名查询")
    @PostMapping("/get/bad")
    private ResultModel getbad(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=materialRealize.getBadPage(getPageModel);
        return Inf.unify(Code.material_ok,sendPageModel,null);
    }
}
