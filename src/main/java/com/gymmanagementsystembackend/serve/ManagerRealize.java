package com.gymmanagementsystembackend.serve;

import com.gymmanagementsystembackend.dao.ManagerMapper;
import com.gymmanagementsystembackend.serve.itf.ManagerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerRealize implements ManagerInterface {

    @Autowired
    private ManagerMapper managerMapper;


}
