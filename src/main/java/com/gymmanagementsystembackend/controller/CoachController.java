package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.CoachTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.CoachRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach")
@Tag(name = "教练管理接口")
public class CoachController {
    @Autowired
    private CoachRealize coachRealize;

    @Operation(summary = "添加教练")
    @PostMapping("/add")
    public ResultModel addCoach(@RequestBody CoachTable coachTable){
        coachRealize.add(coachTable);
        return Inf.unify(Code.coach_ok,null,"添加成功！");
    }

    @Operation(summary = "删除教练",description = "按行id删除教练")
    @DeleteMapping("/delete/one")
    public ResultModel deleteOneCoach(@RequestParam int id){
        coachRealize.deleteOne(id);
        return Inf.unify(Code.coach_ok,null,"删除成功！");
    }

    @Operation(summary = "修改教练")
    @PutMapping("/update")
    public ResultModel updateCoach(@RequestBody CoachTable coachTable){
        coachRealize.update(coachTable);
        return Inf.unify(Code.coach_ok,null,"修改成功！");
    }

    @Operation(summary = "查询教练列表",description = "可按教练姓名查询")
    @PostMapping("/get/page")
    public ResultModel getPageCoach(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=coachRealize.getPageCoach(getPageModel);
        return Inf.unify(Code.coach_ok,sendPageModel,null);
    }
}
