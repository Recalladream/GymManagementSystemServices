package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.GiveLessonsTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.GiveLessonsRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import com.gymmanagementsystembackend.tool.ServeTool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/givelessons")
@Tag(name = "授课管理接口")
public class GiveLessonsController {
    @Autowired
    private GiveLessonsRealize giveLessonsRealize;
    @Autowired
    private ServeTool serveTool;

    @Operation(summary = "添加授课信息")
    @PostMapping("/add")
    public ResultModel add(@RequestBody GiveLessonsTable giveLessonsTable) {
        giveLessonsRealize.add(giveLessonsTable);
        return Inf.unify(Code.givelessons_ok, null, "添加成功！");
    }
    @Operation(summary = "删除授课信息",description = "按行id删除")
    @DeleteMapping("/delete")
    ResultModel delete(@RequestParam int id) {
        giveLessonsRealize.delete(id);
        return Inf.unify(Code.givelessons_ok, null, "删除成功！");
    }

    @Operation(summary = "修改授课信息")
    @PutMapping("/update")
    ResultModel update(@RequestBody GiveLessonsTable giveLessonsTable) {
        giveLessonsRealize.update(giveLessonsTable);
        return Inf.unify(Code.givelessons_ok, null, "修改成功！");
    }

    @Operation(summary = "查询授课信息",description = "可按教练和课程名查询")
    @PostMapping("/get/page")
    ResultModel getPageGive(@RequestBody GetPageModel getPageModel) {
        serveTool.deleteExpiredCourses();
        SendPageModel sendPageModel = giveLessonsRealize.getPage(getPageModel);
        return Inf.unify(Code.givelessons_ok, sendPageModel, null);
    }
}
