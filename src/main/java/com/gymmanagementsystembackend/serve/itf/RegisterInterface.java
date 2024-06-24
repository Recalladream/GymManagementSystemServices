package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.dao.RegisterMapper;
import com.gymmanagementsystembackend.model.RegisterModel;

public interface RegisterInterface {
    public void registerUserAccount(RegisterModel registerModel);
    public long registerUserPhone(String phone);
}
