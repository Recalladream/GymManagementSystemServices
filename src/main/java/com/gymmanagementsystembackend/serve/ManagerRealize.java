package com.gymmanagementsystembackend.serve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.exception.SystemException;
import com.gymmanagementsystembackend.dao.ManagerMapper;
import com.gymmanagementsystembackend.domain.ManagerTable;
import com.gymmanagementsystembackend.domain.RegisterTable;
import com.gymmanagementsystembackend.model.ManagerModel;
import com.gymmanagementsystembackend.serve.itf.ManagerInterface;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.ThreadLocalManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerRealize implements ManagerInterface {

    @Autowired
    private ManagerMapper managerMapper;


}
