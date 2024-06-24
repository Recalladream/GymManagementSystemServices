package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.domain.CourseTable;
import com.gymmanagementsystembackend.model.GetPageModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.model.SendPageModel;
import com.gymmanagementsystembackend.serve.CourseRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/course")
@Tag(name = "课程管理接口")
public class CourseController {
    @Autowired
    private CourseRealize courseRealize;
    @Operation(summary = "添加课程")
    @PostMapping("/add")
    public ResultModel addCourse(@RequestBody CourseTable courseTable){
        courseRealize.addCourse(courseTable);
        return Inf.unify(Code.course_ok,null,"添加成功！");
    }
    @Operation(summary = "删除课程",description = "按行id删除课程")
    @DeleteMapping("/delete/one")
    public ResultModel deleteOne(@RequestParam int id){
        courseRealize.deleteCourse(id);
        return Inf.unify(Code.course_ok,null,"删除成功！");
    }
    @Operation(summary = "修改课程")
    @PutMapping("/update")
    public ResultModel update(@RequestBody CourseTable courseTable){
        courseRealize.updateCourse(courseTable);
        return Inf.unify(Code.course_ok,null,"修改成功！");
    }
    @Operation(summary = "查询课程",description = "可按课程名查询")
    @PostMapping("/get/page")
    public ResultModel getPage(@RequestBody GetPageModel getPageModel){
        SendPageModel sendPageModel=courseRealize.getPageCourse(getPageModel);
        return Inf.unify(Code.course_ok,sendPageModel,null);
    }
}
