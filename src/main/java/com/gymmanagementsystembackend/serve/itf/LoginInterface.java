package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.model.UserLoginModel;

public interface LoginInterface {
    public UserLoginModel loginUserAccount(String account, String password);
    public UserLoginModel loginUserPhone(String phone,String code);
    public String loginManagerAccount(String account,String password);
    public void getPhoneLoginCode(String phone);
    public void getAccountRegisterCode(String phone);
}
