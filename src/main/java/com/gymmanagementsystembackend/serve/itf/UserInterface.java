package com.gymmanagementsystembackend.serve.itf;

import com.gymmanagementsystembackend.model.*;

public interface UserInterface {
    public void updateUser(UserModel userModel);
    public SendPageModel getYiBooked(GetPageModel getPageModel);
    public UserModel getUserInf(String vipId);
    public String openVip(VipModel vipModel);
    public void UserSigNin(SigninModel signinModel);
}
