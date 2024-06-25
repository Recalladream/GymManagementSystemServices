package com.gymmanagementsystembackend.controller;

import com.gymmanagementsystembackend.model.ManagerModel;
import com.gymmanagementsystembackend.model.ResultModel;
import com.gymmanagementsystembackend.serve.ManagerRealize;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.Inf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@Tag(name = "管理员管理接口")
public class ManagerController {
    @Autowired
    private ManagerRealize managerRealize;

}
